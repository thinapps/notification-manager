package top.thinapps.notificationmanager

import android.content.Intent
import android.content.pm.ApplicationInfo
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

    fun getAppEntry(packageName: String): AppEntry {
        return AppEntry(
            label = resolveApplicationLabel(packageName),
            packageName = packageName
        )
    }

    private fun resolveApplicationLabel(packageName: String): String {
        return try {
            getApplicationInfo(packageName)
                .loadLabel(packageManager)
                .toString()
        } catch (_: PackageManager.NameNotFoundException) {
            packageName
        } catch (_: RuntimeException) {
            packageName
        }
    }

    private fun getApplicationInfo(packageName: String): ApplicationInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getApplicationInfo(packageName, 0)
        }
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
