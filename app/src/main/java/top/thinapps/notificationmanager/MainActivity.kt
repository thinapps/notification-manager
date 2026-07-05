package top.thinapps.notificationmanager

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
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
    private val notificationAuditRefreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            refreshNotificationAuditUi()
        }
    }
    private lateinit var notificationAuditSummaryText: TextView
    private var allApps: List<AppEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allApps = appsRepository.getLaunchableApps()
        setupDeviceStatusButtons()
        setupNotificationAuditCard()
        setupSearch()
        renderDeviceStatus()
        renderNotificationAuditStatus()
        renderApps(allApps)
    }

    override fun onStart() {
        super.onStart()
        registerNotificationAuditRefreshReceiver()
    }

    override fun onResume() {
        super.onResume()
        refreshNotificationAuditSnapshotIfPossible()
        renderDeviceStatus()
        refreshNotificationAuditUi()
    }

    override fun onStop() {
        unregisterReceiver(notificationAuditRefreshReceiver)
        super.onStop()
    }

    private fun setupDeviceStatusButtons() {
        findViewById<Button>(R.id.soundSettingsButton).setOnClickListener { view ->
            view.performButtonHaptic()
            settingsNavigator.openSoundSettings()
        }
        findViewById<Button>(R.id.doNotDisturbSettingsButton).setOnClickListener { view ->
            view.performButtonHaptic()
            settingsNavigator.openDoNotDisturbSettings()
        }
        findViewById<Button>(R.id.systemSettingsButton).setOnClickListener { view ->
            view.performButtonHaptic()
            settingsNavigator.openSystemNotificationSettings()
        }
    }

    private fun setupNotificationAuditCard() {
        val root = findViewById<View>(R.id.deviceStatusCard).parent as LinearLayout
        val deviceStatusCard = findViewById<View>(R.id.deviceStatusCard)
        root.addView(createNotificationAuditCard(), root.indexOfChild(deviceStatusCard) + 1)
    }

    private fun createNotificationAuditCard(): View {
        return LinearLayout(this).apply {
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
                topMargin = resources.getDimensionPixelSize(R.dimen.space_large)
            }

            addView(TextView(this@MainActivity).apply {
                text = getString(R.string.notification_audit_title)
                setTextColor(getColor(R.color.text_primary))
                textSize = resources.getDimension(R.dimen.app_label_text_size) / resources.displayMetrics.scaledDensity
                setTypeface(typeface, Typeface.BOLD)
            })

            notificationAuditSummaryText = TextView(this@MainActivity).apply {
                setTextColor(getColor(R.color.text_secondary))
                textSize = resources.getDimension(R.dimen.package_text_size) / resources.displayMetrics.scaledDensity
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = resources.getDimensionPixelSize(R.dimen.space_small)
                }
            }
            addView(notificationAuditSummaryText)

            addView(Button(this@MainActivity).apply {
                text = getString(R.string.open_notification_access_settings)
                setOnClickListener { view ->
                    view.performButtonHaptic()
                    settingsNavigator.openNotificationListenerSettings()
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = resources.getDimensionPixelSize(R.dimen.space_medium)
                }
            })
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

    private fun renderNotificationAuditStatus() {
        notificationAuditSummaryText.text = when {
            !isNotificationAuditEnabled() -> getString(R.string.notification_audit_disabled_summary)
            !NotificationAuditState.isListenerConnected() -> getString(R.string.notification_audit_connecting_summary)
            !NotificationAuditState.hasSnapshot() -> getString(R.string.notification_audit_waiting_summary)
            else -> getString(R.string.notification_audit_enabled_summary)
        }
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
        val auditEnabled = isNotificationAuditEnabled()
        val auditSnapshotLoaded = NotificationAuditState.hasSnapshot()
        val listenerConnected = NotificationAuditState.isListenerConnected()
        val auditSnapshot = NotificationAuditState.snapshot()

        countText.text = resources.getQuantityString(R.plurals.app_count, apps.size, apps.size)
        emptyText.text = if (allApps.isEmpty()) {
            getString(R.string.no_apps_found)
        } else {
            getString(R.string.no_matching_apps)
        }
        emptyText.visibility = if (apps.isEmpty()) View.VISIBLE else View.GONE
        appList.removeAllViews()

        apps.forEach { app ->
            appList.addView(createAppRow(app, auditEnabled, listenerConnected, auditSnapshotLoaded, auditSnapshot[app.packageName]))
        }
    }

    private fun createAppRow(
        app: AppEntry,
        auditEnabled: Boolean,
        listenerConnected: Boolean,
        auditSnapshotLoaded: Boolean,
        audit: AppNotificationAudit?
    ): View {
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

        row.addView(TextView(this).apply {
            text = when {
                !auditEnabled -> getString(R.string.audit_disabled)
                !listenerConnected -> getString(R.string.audit_waiting_for_listener)
                !auditSnapshotLoaded -> getString(R.string.audit_waiting_for_results)
                audit != null -> audit.summary
                else -> getString(R.string.audit_no_active_notifications)
            }
            setTextColor(getColor(R.color.text_secondary))
            textSize = resources.getDimension(R.dimen.package_text_size) / resources.displayMetrics.scaledDensity
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.space_small)
            }
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
                setOnClickListener { view ->
                    view.performButtonHaptic()
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
                setOnClickListener { view ->
                    view.performButtonHaptic()
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

    private fun refreshNotificationAuditSnapshotIfPossible() {
        if (!isNotificationAuditEnabled()) {
            NotificationAuditState.clear()
            return
        }

        if (!NotificationAuditListenerService.refreshActiveSnapshot()) {
            requestNotificationAuditRebindIfNeeded()
        }
    }

    private fun refreshNotificationAuditUi() {
        renderNotificationAuditStatus()
        renderApps(filterApps(findViewById<EditText>(R.id.searchInput).text?.toString().orEmpty()))
    }

    private fun requestNotificationAuditRebindIfNeeded() {
        if (isNotificationAuditEnabled() && !NotificationAuditState.isListenerConnected()) {
            val component = ComponentName(this, NotificationAuditListenerService::class.java)
            ensureNotificationAuditComponentEnabled(component)
            NotificationListenerService.requestRebind(component)
        }
    }

    private fun ensureNotificationAuditComponentEnabled(component: ComponentName) {
        packageManager.setComponentEnabledSetting(
            component,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun registerNotificationAuditRefreshReceiver() {
        val filter = IntentFilter(NotificationAuditListenerService.ACTION_NOTIFICATION_AUDIT_UPDATED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationAuditRefreshReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(notificationAuditRefreshReceiver, filter)
        }
    }

    private fun isNotificationAuditEnabled(): Boolean {
        val component = ComponentName(this, NotificationAuditListenerService::class.java)
        val enabledListeners = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS)
            ?: return false

        return enabledListeners.split(':').any { flattenedComponent ->
            ComponentName.unflattenFromString(flattenedComponent)?.let { enabledComponent ->
                enabledComponent.packageName == packageName && enabledComponent.className == component.className
            } == true
        }
    }

    private fun View.performButtonHaptic() {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    private companion object {
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    }
}
