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
                    val classifications = entry.value.map { classify(it, rankingMap) }
                    val strongest = classifications.maxByOrNull { classification ->
                        rankLabel(classification.label)
                    } ?: NotificationAuditClassification(
                        label = LABEL_UNKNOWN,
                        detail = DETAIL_NO_RANKING_DATA
                    )
                    AppNotificationAudit(
                        label = strongest.label,
                        detail = strongest.detail,
                        activeCount = entry.value.size
                    )
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
    ): NotificationAuditClassification {
        val ranking = NotificationListenerService.Ranking()
        val hasRanking = rankingMap?.getRanking(statusBarNotification.key, ranking) == true
        val channel = if (hasRanking) ranking.channel else null
        val importance = if (hasRanking) {
            ranking.importance
        } else {
            NotificationManager.IMPORTANCE_UNSPECIFIED
        }

        return classifyChannel(channel, importance)
    }

    private fun classifyChannel(
        channel: NotificationChannel?,
        importance: Int
    ): NotificationAuditClassification {
        val resolvedImportance = channel?.importance ?: importance
        val importanceText = importanceLabel(resolvedImportance)
        val hasSound = channel?.sound != null
        val hasVibration = channel?.shouldVibrate() == true

        return when {
            resolvedImportance == NotificationManager.IMPORTANCE_NONE -> NotificationAuditClassification(
                label = LABEL_NONE,
                detail = "blocked channel"
            )
            resolvedImportance == NotificationManager.IMPORTANCE_MIN -> NotificationAuditClassification(
                label = LABEL_SILENT,
                detail = "$importanceText importance"
            )
            resolvedImportance == NotificationManager.IMPORTANCE_LOW -> NotificationAuditClassification(
                label = LABEL_SILENT,
                detail = "$importanceText importance"
            )
            hasSound && hasVibration -> NotificationAuditClassification(
                label = LABEL_SOUND_AND_VIBRATE,
                detail = "$importanceText importance"
            )
            hasSound -> NotificationAuditClassification(
                label = LABEL_SOUND,
                detail = "$importanceText importance"
            )
            hasVibration -> NotificationAuditClassification(
                label = LABEL_VIBRATE,
                detail = "$importanceText importance"
            )
            resolvedImportance >= NotificationManager.IMPORTANCE_DEFAULT -> NotificationAuditClassification(
                label = LABEL_ALERTING,
                detail = "$importanceText importance, no sound/vibration exposed"
            )
            else -> NotificationAuditClassification(
                label = LABEL_UNKNOWN,
                detail = DETAIL_NO_RANKING_DATA
            )
        }
    }

    private fun importanceLabel(importance: Int): String {
        return when (importance) {
            NotificationManager.IMPORTANCE_NONE -> "Blocked"
            NotificationManager.IMPORTANCE_MIN -> "Min"
            NotificationManager.IMPORTANCE_LOW -> "Low"
            NotificationManager.IMPORTANCE_DEFAULT -> "Default"
            NotificationManager.IMPORTANCE_HIGH -> "High"
            else -> "Unknown"
        }
    }

    private fun rankLabel(label: String): Int {
        return when (label) {
            LABEL_SOUND_AND_VIBRATE -> 5
            LABEL_SOUND -> 4
            LABEL_VIBRATE -> 3
            LABEL_ALERTING -> 3
            LABEL_SILENT -> 2
            LABEL_NONE -> 1
            else -> 0
        }
    }

    private data class NotificationAuditClassification(
        val label: String,
        val detail: String
    )

    companion object {
        const val ACTION_NOTIFICATION_AUDIT_UPDATED = "top.thinapps.notificationmanager.NOTIFICATION_AUDIT_UPDATED"
        @Volatile private var activeService: NotificationAuditListenerService? = null

        fun refreshActiveSnapshot(): Boolean {
            return activeService?.refreshAudit() == true
        }

        private const val LABEL_SOUND_AND_VIBRATE = "Sound + vibrate"
        private const val LABEL_SOUND = "Sound"
        private const val LABEL_VIBRATE = "Vibrate"
        private const val LABEL_ALERTING = "Alerting"
        private const val LABEL_SILENT = "Silent"
        private const val LABEL_NONE = "None"
        private const val LABEL_UNKNOWN = "Unknown"
        private const val DETAIL_NO_RANKING_DATA = "ranking details not exposed"
    }
}
