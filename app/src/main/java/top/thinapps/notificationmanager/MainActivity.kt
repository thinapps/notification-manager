package top.thinapps.notificationmanager

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val appsRepository by lazy { InstalledAppsRepository(packageManager) }
    private val settingsNavigator by lazy { NotificationSettingsNavigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        renderApps()
    }

    private fun renderApps() {
        val apps = appsRepository.getLaunchableApps()
        val appList = findViewById<LinearLayout>(R.id.appList)
        val emptyText = findViewById<TextView>(R.id.emptyText)

        emptyText.visibility = if (apps.isEmpty()) View.VISIBLE else View.GONE
        appList.removeAllViews()

        apps.forEach { app ->
            appList.addView(createAppRow(app))
        }
    }

    private fun createAppRow(app: AppEntry): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = getDrawable(R.drawable.app_row_background)
            setPadding(
                resources.getDimensionPixelSize(R.dimen.app_row_padding),
                resources.getDimensionPixelSize(R.dimen.app_row_padding),
                resources.getDimensionPixelSize(R.dimen.app_row_padding),
                resources.getDimensionPixelSize(R.dimen.app_row_padding)
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = resources.getDimensionPixelSize(R.dimen.space_medium)
            }
        }

        row.addView(TextView(this).apply {
            text = app.label
            setTextColor(getColor(R.color.text_primary))
            textSize = resources.getDimension(R.dimen.app_label_text_size) / resources.displayMetrics.scaledDensity
        })

        row.addView(TextView(this).apply {
            text = app.packageName
            setTextColor(getColor(R.color.text_secondary))
            textSize = resources.getDimension(R.dimen.package_text_size) / resources.displayMetrics.scaledDensity
        })

        row.addView(Button(this).apply {
            text = getString(R.string.open_notification_settings)
            setOnClickListener {
                settingsNavigator.openAppNotificationSettings(app.packageName)
            }
        })

        return row
    }
}
