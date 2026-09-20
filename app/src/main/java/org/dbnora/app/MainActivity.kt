package org.dbnora.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { DBNoraScreen() } }
        val onboarding = getSharedPreferences(IntroActivity.PREFERENCES, MODE_PRIVATE)
        if (!onboarding.getBoolean(IntroActivity.INTRO_SEEN, false)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }
    }

    @Composable
    private fun DBNoraScreen() {
        fun parseDecimal(text: String): Double? =
            text.trim().replace(',', '.').toDoubleOrNull()

        var offsetText by remember {
            mutableStateOf(getSharedPreferences("settings", MODE_PRIVATE)
                .getFloat("calibration_offset", 100f).toString())
        }
        var referenceText by remember { mutableStateOf("") }
        var currentDb by remember { mutableStateOf<Double?>(null) }
        var calibrationSaved by remember { mutableStateOf(false) }
        var showCalibration by remember { mutableStateOf(false) }
        val settings = getSharedPreferences("settings", MODE_PRIVATE)

        LaunchedEffect(Unit) {
            while (true) {
                val value = settings.getFloat("last_estimated_db", Float.NaN)
                currentDb = value.takeUnless { it.isNaN() }?.toDouble()
                delay(250)
            }
        }
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity, Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) startMonitor()
        }

        Surface(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                Text(stringResource(R.string.summary))
                Text(
                    text = currentDb?.let {
                        stringResource(R.string.live_level, it)
                    } ?: stringResource(R.string.live_level_waiting),
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = {
                    parseDecimal(offsetText)?.toFloat()?.let {
                        settings.edit().putFloat("calibration_offset", it).apply()
                    }
                    val permissions = buildList {
                        add(Manifest.permission.RECORD_AUDIO)
                        if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
                    }.toTypedArray()
                    permissionLauncher.launch(permissions)
                }) { Text(stringResource(R.string.start_monitoring)) }
                OutlinedButton(onClick = {
                    startService(Intent(this@MainActivity, NoiseMonitorService::class.java)
                        .setAction(NoiseMonitorService.ACTION_STOP))
                }) { Text(stringResource(R.string.stop_monitoring)) }
                OutlinedButton(onClick = {
                    startActivity(Intent(this@MainActivity, IntroActivity::class.java))
                }) { Text(stringResource(R.string.introduction)) }
                OutlinedButton(onClick = {
                    startActivity(Intent(this@MainActivity, HelpActivity::class.java))
                }) { Text(stringResource(R.string.how_to_use)) }
                Text(stringResource(R.string.works_without_calibration))
                OutlinedButton(onClick = { showCalibration = !showCalibration }) {
                    Text(
                        stringResource(
                            if (showCalibration) R.string.hide_calibration
                            else R.string.optional_calibration
                        )
                    )
                }
                if (showCalibration) {
                    Card(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                stringResource(R.string.how_to_calibrate),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(stringResource(R.string.calibration_steps))
                        }
                    }
                    OutlinedTextField(
                        value = offsetText,
                        onValueChange = { offsetText = it },
                        label = { Text(stringResource(R.string.calibration_offset)) },
                        supportingText = { Text(stringResource(R.string.calibration_help)) }
                    )
                    OutlinedTextField(
                        value = referenceText,
                        onValueChange = {
                            referenceText = it
                            calibrationSaved = false
                        },
                        label = { Text(stringResource(R.string.reference_level)) },
                        supportingText = { Text(stringResource(R.string.reference_help)) }
                    )
                    Button(
                        enabled = currentDb != null && parseDecimal(referenceText) != null,
                        onClick = {
                            val oldOffset = parseDecimal(offsetText) ?: 100.0
                            val reference = parseDecimal(referenceText) ?: return@Button
                            val measured = currentDb ?: return@Button
                            val newOffset = oldOffset + reference - measured
                            offsetText = String.format(Locale.US, "%.1f", newOffset)
                            settings.edit().putFloat("calibration_offset", newOffset.toFloat()).apply()
                            calibrationSaved = true
                        }
                    ) { Text(stringResource(R.string.calibrate)) }
                    if (calibrationSaved) {
                        Text(stringResource(R.string.calibration_saved, offsetText))
                    }
                }
                Text(stringResource(R.string.privacy_notice))
            }
        }
    }

    private fun startMonitor() {
        ContextCompat.startForegroundService(
            this, Intent(this, NoiseMonitorService::class.java)
                .setAction(NoiseMonitorService.ACTION_START)
        )
    }
}
