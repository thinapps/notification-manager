package top.thinapps.notificationmanager

data class AppNotificationAudit(
    val label: String,
    val activeCount: Int
) {
    val summary: String
        get() {
            val activeText = if (activeCount == 1) "1 active" else "$activeCount active"
            return "Audit: $label ($activeText)"
        }
}

object NotificationAuditState {
    private val lock = Any()
    private var auditsByPackage: Map<String, AppNotificationAudit> = emptyMap()

    fun snapshot(): Map<String, AppNotificationAudit> {
        return synchronized(lock) {
            auditsByPackage.toMap()
        }
    }

    fun clear() {
        synchronized(lock) {
            auditsByPackage = emptyMap()
        }
    }

    fun replace(nextAudits: Map<String, AppNotificationAudit>) {
        synchronized(lock) {
            auditsByPackage = nextAudits.toMap()
        }
    }
}
