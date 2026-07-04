package top.thinapps.notificationmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationAuditListenerService : NotificationListenerService() {
    override fun onListenerConnected() {
        activeService = this
        NotificationAuditState.markListenerConnected()
        refreshAudit()
    }

    override fun onListenerDisconnected() {
        if (activeService === this) {
            activeService = null
        }
        NotificationAuditState.markListenerDisconnected()
        NotificationAuditState.clear()
        broadcastAuditUpdated()
        requestRebind(ComponentName(this, NotificationAuditListenerService::class.java))
    }

    override fun onDestroy() {
        if (activeService === this) {
            activeService = null
            NotificationAuditState.markListenerDisconnected()
            broadcastAuditUpdated()
        }
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        refreshAudit()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        refreshAudit()
    }

    override fun onNotificationRankingUpdate(rankingMap: NotificationListenerService.RankingMap) {
        refreshAudit()
    }

    private fun refreshAudit(): Boolean {
        return try {
            val rankingMap = currentRanking
            val nextAudits = getActiveNotifications()
                ?.groupBy(StatusBarNotification::getPackageName)
                .orEmpty()
                .mapValues { entry ->
                    val labels = entry.value.map { classify(it, rankingMap) }
                    val strongest = labels.maxByOrNull(::rankLabel) ?: LABEL_UNKNOWN
                    AppNotificationAudit(label = strongest, activeCount = entry.value.size)
                }

            NotificationAuditState.replace(nextAudits)
            broadcastAuditUpdated()
            true
        } catch (_: RuntimeException) {
            NotificationAuditState.clear()
            broadcastAuditUpdated()
            false
        }
    }

    private fun broadcastAuditUpdated() {
        sendBroadcast(Intent(ACTION_NOTIFICATION_AUDIT_UPDATED).setPackage(packageName))
    }

    private fun classify(
        statusBarNotification: StatusBarNotification,
        rankingMap: NotificationListenerService.RankingMap?
    ): String {
        val ranking = NotificationListenerService.Ranking()
        val hasRanking = rankingMap?.getRanking(statusBarNotification.key, ranking) == true
        val channel = if (hasRanking) ranking.channel else null
        val importance = if (hasRanking) {
            ranking.importance
        } else {
            channel?.importance ?: NotificationManager.IMPORTANCE_UNSPECIFIED
        }

        return classifyChannel(channel, importance)
    }

    private fun classifyChannel(
        channel: NotificationChannel?,
        importance: Int
    ): String {
        val resolvedImportance = channel?.importance ?: importance

        return when {
            resolvedImportance == NotificationManager.IMPORTANCE_NONE -> LABEL_NONE
            resolvedImportance == NotificationManager.IMPORTANCE_MIN -> LABEL_SILENT
            resolvedImportance == NotificationManager.IMPORTANCE_LOW -> LABEL_SILENT
            channel?.sound != null -> LABEL_SOUND
            channel?.shouldVibrate() == true -> LABEL_VIBRATE
            resolvedImportance >= NotificationManager.IMPORTANCE_DEFAULT -> LABEL_SOUND
            else -> LABEL_UNKNOWN
        }
    }

    private fun rankLabel(label: String): Int {
        return when (label) {
            LABEL_SOUND -> 4
            LABEL_VIBRATE -> 3
            LABEL_SILENT -> 2
            LABEL_NONE -> 1
            else -> 0
        }
    }

    companion object {
        const val ACTION_NOTIFICATION_AUDIT_UPDATED = "top.thinapps.notificationmanager.NOTIFICATION_AUDIT_UPDATED"
        @Volatile private var activeService: NotificationAuditListenerService? = null

        fun refreshActiveSnapshot(): Boolean {
            return activeService?.refreshAudit() == true
        }

        private const val LABEL_SOUND = "Sound"
        private const val LABEL_VIBRATE = "Vibrate"
        private const val LABEL_SILENT = "Silent"
        private const val LABEL_NONE = "None"
        private const val LABEL_UNKNOWN = "Unknown"
    }
}
