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
    private var hasSnapshot = false
    private var listenerConnected = false

    fun snapshot(): Map<String, AppNotificationAudit> {
        return synchronized(lock) {
            auditsByPackage.toMap()
        }
    }

    fun hasSnapshot(): Boolean {
        return synchronized(lock) {
            hasSnapshot
        }
    }

    fun isListenerConnected(): Boolean {
        return synchronized(lock) {
            listenerConnected
        }
    }

    fun markListenerConnected() {
        synchronized(lock) {
            listenerConnected = true
        }
    }

    fun markListenerDisconnected() {
        synchronized(lock) {
            listenerConnected = false
        }
    }

    fun clear() {
        synchronized(lock) {
            auditsByPackage = emptyMap()
            hasSnapshot = false
        }
    }

    fun replace(nextAudits: Map<String, AppNotificationAudit>) {
        synchronized(lock) {
            auditsByPackage = nextAudits.toMap()
            hasSnapshot = true
        }
    }
}
