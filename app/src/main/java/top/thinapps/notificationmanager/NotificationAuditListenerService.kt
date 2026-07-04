package top.thinapps.notificationmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationAuditListenerService : NotificationListenerService() {
    override fun onListenerConnected() {
        refreshAudit()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        refreshAudit()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        refreshAudit()
    }

    override fun onNotificationRankingUpdate(rankingMap: RankingMap) {
        refreshAudit()
    }

    private fun refreshAudit() {
        try {
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
        } catch (_: RuntimeException) {
            NotificationAuditState.clear()
        }
    }

    private fun classify(
        statusBarNotification: StatusBarNotification,
        rankingMap: RankingMap?
    ): String {
        val ranking = Ranking()
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

    private companion object {
        const val LABEL_SOUND = "Sound"
        const val LABEL_VIBRATE = "Vibrate"
        const val LABEL_SILENT = "Silent"
        const val LABEL_NONE = "None"
        const val LABEL_UNKNOWN = "Unknown"
    }
}
