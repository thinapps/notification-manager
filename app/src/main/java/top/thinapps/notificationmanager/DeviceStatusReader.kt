package top.thinapps.notificationmanager

import android.app.Activity
import android.app.NotificationManager
import android.media.AudioManager
import kotlin.math.roundToInt

data class DeviceStatus(
    val soundMode: String,
    val doNotDisturbMode: String,
    val ringVolume: String,
    val notificationVolume: String,
    val alarmVolume: String,
    val callVolume: String,
    val mediaVolume: String,
    val systemVolume: String
)

class DeviceStatusReader(
    activity: Activity
) {
    private val audioManager = activity.getSystemService(AudioManager::class.java)
    private val notificationManager = activity.getSystemService(NotificationManager::class.java)

    fun read(): DeviceStatus {
        return DeviceStatus(
            soundMode = readSoundMode(),
            doNotDisturbMode = readDoNotDisturbMode(),
            ringVolume = readVolume(AudioManager.STREAM_RING),
            notificationVolume = readVolume(AudioManager.STREAM_NOTIFICATION),
            alarmVolume = readVolume(AudioManager.STREAM_ALARM),
            callVolume = readVolume(AudioManager.STREAM_VOICE_CALL),
            mediaVolume = readVolume(AudioManager.STREAM_MUSIC),
            systemVolume = readVolume(AudioManager.STREAM_SYSTEM)
        )
    }

    private fun readSoundMode(): String {
        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> "Ring"
            AudioManager.RINGER_MODE_VIBRATE -> "Vibrate"
            AudioManager.RINGER_MODE_SILENT -> "Silent"
            else -> "Unknown"
        }
    }

    private fun readDoNotDisturbMode(): String {
        return when (notificationManager.currentInterruptionFilter) {
            NotificationManager.INTERRUPTION_FILTER_ALL -> "Off"
            NotificationManager.INTERRUPTION_FILTER_PRIORITY -> "Priority only"
            NotificationManager.INTERRUPTION_FILTER_ALARMS -> "Alarms only"
            NotificationManager.INTERRUPTION_FILTER_NONE -> "Total silence"
            NotificationManager.INTERRUPTION_FILTER_UNKNOWN -> "Unknown"
            else -> "Unknown"
        }
    }

    private fun readVolume(streamType: Int): String {
        val currentVolume = audioManager.getStreamVolume(streamType)
        val maxVolume = audioManager.getStreamMaxVolume(streamType)

        if (maxVolume <= 0) {
            return "Unknown"
        }

        val percentage = ((currentVolume.toFloat() / maxVolume.toFloat()) * 100f).roundToInt()
        return "$percentage% ($currentVolume/$maxVolume)"
    }
}
