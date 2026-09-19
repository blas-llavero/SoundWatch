package cat.blas.soundwatch

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { SoundWatchScreen() } }
    }

    @Composable
    private fun SoundWatchScreen() {
        var offsetText by remember {
            mutableStateOf(getSharedPreferences("settings", MODE_PRIVATE)
                .getFloat("calibration_offset", 100f).toString())
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
                Modifier.padding(24.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SoundWatch", style = MaterialTheme.typography.headlineLarge)
                Text("Alerta si el soroll estimat supera 80 dB durant 1 segon. Després espera 1 minut.")
                OutlinedTextField(
                    value = offsetText,
                    onValueChange = { offsetText = it },
                    label = { Text("Desplaçament de calibratge (dB)") },
                    supportingText = { Text("Calibra'l amb un sonòmetre; el valor 100 és només orientatiu.") }
                )
                Button(onClick = {
                    offsetText.toFloatOrNull()?.let {
                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                            .putFloat("calibration_offset", it).apply()
                    }
                    val permissions = buildList {
                        add(Manifest.permission.RECORD_AUDIO)
                        if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
                    }.toTypedArray()
                    permissionLauncher.launch(permissions)
                }) { Text("Inicia el monitoratge") }
                OutlinedButton(onClick = {
                    startService(Intent(this@MainActivity, NoiseMonitorService::class.java)
                        .setAction(NoiseMonitorService.ACTION_STOP))
                }) { Text("Atura el monitoratge") }
                Text("Privadesa: l'àudio es processa al telèfon i no es desa ni s'envia.")
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
