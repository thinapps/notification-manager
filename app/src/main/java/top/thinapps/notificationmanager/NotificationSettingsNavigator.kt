package top.thinapps.notificationmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class NotificationSettingsNavigator(
    private val activity: Activity
) {
    fun openSystemNotificationSettings() {
        if (!start(Intent(Settings.ACTION_NOTIFICATION_SETTINGS))) {
            start(Intent(Settings.ACTION_SETTINGS))
        }
    }

    fun openAppNotificationSettings(packageName: String) {
        val notificationSettings = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }

        if (!start(notificationSettings)) {
            openAppDetails(packageName)
        }
    }

    fun openAppDetails(packageName: String) {
        val appDetails = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }

        start(appDetails)
    }

    private fun start(intent: Intent): Boolean {
        return try {
            if (intent.resolveActivity(activity.packageManager) == null) {
                false
            } else {
                activity.startActivity(intent)
                true
            }
        } catch (_: RuntimeException) {
            false
        }
    }
}
