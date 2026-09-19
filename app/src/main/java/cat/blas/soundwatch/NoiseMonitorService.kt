package cat.blas.soundwatch

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread
import kotlin.math.log10
import kotlin.math.sqrt

class NoiseMonitorService : Service() {
    companion object {
        const val ACTION_START = "cat.blas.soundwatch.START"
        const val ACTION_STOP = "cat.blas.soundwatch.STOP"
        private const val SERVICE_CHANNEL = "soundwatch_monitor"
        private const val ALERT_CHANNEL = "soundwatch_alerts"
        private const val SERVICE_ID = 100
        private const val ALERT_ID = 101
        private const val SAMPLE_RATE = 16_000
    }

    @Volatile private var running = false
    private var recorder: AudioRecord? = null
    private val gate = NoiseGate()

    override fun onCreate() {
        super.onCreate()
        createChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopMonitoring()
            return START_NOT_STICKY
        }
        startForeground(SERVICE_ID, monitoringNotification())
        if (!running) startMonitoring()
        return START_STICKY
    }

    private fun startMonitoring() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            stopSelf(); return
        }
        val min = AudioRecord.getMinBufferSize(
            SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
        )
        recorder = AudioRecord(
            MediaRecorder.AudioSource.UNPROCESSED,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            maxOf(min, SAMPLE_RATE)
        )
        running = true
        recorder?.startRecording()
        thread(name = "SoundWatch-recorder") {
            val samples = ShortArray(1_600) // about 100 ms
            while (running) {
                val count = recorder?.read(samples, 0, samples.size) ?: -1
                if (count > 0) {
                    var sumSquares = 0.0
                    for (index in 0 until count) {
                        val value = samples[index].toDouble()
                        sumSquares += value * value
                    }
                    val rms = sqrt(sumSquares / count)
                    val dbFs = if (rms > 0) 20.0 * log10(rms / Short.MAX_VALUE) else -120.0
                    val offset = getSharedPreferences("settings", MODE_PRIVATE)
                        .getFloat("calibration_offset", 100f).toDouble()
                    val estimatedDbSpl = dbFs + offset
                    if (gate.sample(estimatedDbSpl, System.currentTimeMillis())) sendAlert(estimatedDbSpl)
                }
            }
        }
    }

    private fun sendAlert(db: Double) {
        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(getString(R.string.alert_title))
            .setContentText(getString(R.string.alert_text, db))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()
        getSystemService(NotificationManager::class.java).notify(ALERT_ID, notification)
    }

    private fun monitoringNotification(): Notification {
        val stopIntent = Intent(this, NoiseMonitorService::class.java).setAction(ACTION_STOP)
        val pendingStop = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, SERVICE_CHANNEL)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentTitle(getString(R.string.monitor_title))
            .setContentText(getString(R.string.monitor_text))
            .setOngoing(true)
            .addAction(0, getString(R.string.stop), pendingStop)
            .build()
    }

    private fun createChannels() {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(NotificationChannel(
            SERVICE_CHANNEL, getString(R.string.monitor_channel), NotificationManager.IMPORTANCE_LOW
        ))
        manager.createNotificationChannel(NotificationChannel(
            ALERT_CHANNEL, getString(R.string.alert_channel), NotificationManager.IMPORTANCE_HIGH
        ).apply { description = getString(R.string.alert_channel_description) })
    }

    private fun stopMonitoring() {
        running = false
        runCatching { recorder?.stop() }
        recorder?.release()
        recorder = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() { stopMonitoring(); super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}
