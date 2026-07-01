package top.thinapps.notificationmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import java.util.Locale

class InstalledAppsRepository(
    private val packageManager: PackageManager
) {
    fun getLaunchableApps(): List<AppEntry> {
        val launchIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return queryLauncherActivities(launchIntent)
            .map { resolveInfo ->
                AppEntry(
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName
                )
            }
            .distinctBy { app -> app.packageName }
            .sortedBy { app -> app.label.lowercase(Locale.ROOT) }
    }

    private fun queryLauncherActivities(intent: Intent): List<ResolveInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, 0)
        }
    }
}
