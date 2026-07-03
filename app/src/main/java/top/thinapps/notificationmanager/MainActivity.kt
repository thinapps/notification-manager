package top.thinapps.notificationmanager

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity() {
    private val appsRepository by lazy { InstalledAppsRepository(packageManager) }
    private val deviceStatusReader by lazy { DeviceStatusReader(this) }
    private val settingsNavigator by lazy { NotificationSettingsNavigator(this) }
    private var allApps: List<AppEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allApps = appsRepository.getLaunchableApps()
        setupDeviceStatusButtons()
        setupSearch()
        renderDeviceStatus()
        renderApps(allApps)
    }

    override fun onResume() {
        super.onResume()
        renderDeviceStatus()
    }

    private fun setupDeviceStatusButtons() {
        findViewById<Button>(R.id.soundSettingsButton).setOnClickListener {
            settingsNavigator.openSoundSettings()
        }
        findViewById<Button>(R.id.doNotDisturbSettingsButton).setOnClickListener {
            settingsNavigator.openDoNotDisturbSettings()
        }
        findViewById<Button>(R.id.systemSettingsButton).setOnClickListener {
            settingsNavigator.openSystemNotificationSettings()
        }
    }

    private fun renderDeviceStatus() {
        val status = deviceStatusReader.read()

        findViewById<TextView>(R.id.soundModeValueText).text = status.soundMode
        findViewById<TextView>(R.id.doNotDisturbValueText).text = status.doNotDisturbMode
        findViewById<TextView>(R.id.ringVolumeValueText).text = status.ringVolume
        findViewById<TextView>(R.id.notificationVolumeValueText).text = status.notificationVolume
        findViewById<TextView>(R.id.alarmVolumeValueText).text = status.alarmVolume
        findViewById<TextView>(R.id.callVolumeValueText).text = status.callVolume
        findViewById<TextView>(R.id.mediaVolumeValueText).text = status.mediaVolume
        findViewById<TextView>(R.id.systemVolumeValueText).text = status.systemVolume
        findViewById<TextView>(R.id.accessibilityVolumeValueText).text = status.accessibilityVolume
        findViewById<TextView>(R.id.keypadToneVolumeValueText).text = status.keypadToneVolume
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.searchInput).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                renderApps(filterApps(s?.toString().orEmpty()))
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun filterApps(query: String): List<AppEntry> {
        val normalizedQuery = query.trim().lowercase(Locale.ROOT)
        if (normalizedQuery.isEmpty()) {
            return allApps
        }

        return allApps.filter { app ->
            app.label.lowercase(Locale.ROOT).contains(normalizedQuery) ||
                app.packageName.lowercase(Locale.ROOT).contains(normalizedQuery)
        }
    }

    private fun renderApps(apps: List<AppEntry>) {
        val appList = findViewById<LinearLayout>(R.id.appList)
        val countText = findViewById<TextView>(R.id.countText)
        val emptyText = findViewById<TextView>(R.id.emptyText)

        countText.text = resources.getQuantityString(R.plurals.app_count, apps.size, apps.size)
        emptyText.text = if (allApps.isEmpty()) {
            getString(R.string.no_apps_found)
        } else {
            getString(R.string.no_matching_apps)
        }
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

        row.addView(createButtonRow(app))

        return row
    }

    private fun createButtonRow(app: AppEntry): View {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.space_medium)
            }

            addView(Button(this@MainActivity).apply {
                text = getString(R.string.open_notification_settings)
                setOnClickListener {
                    settingsNavigator.openAppNotificationSettings(app.packageName)
                }
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    marginEnd = resources.getDimensionPixelSize(R.dimen.button_row_gap)
                }
            })

            addView(Button(this@MainActivity).apply {
                text = getString(R.string.open_app_info)
                setOnClickListener {
                    settingsNavigator.openAppDetails(app.packageName)
                }
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            })
        }
    }
}
