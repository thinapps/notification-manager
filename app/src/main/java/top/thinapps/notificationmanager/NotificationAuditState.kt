package top.thinapps.notificationmanager

data class AppNotificationAudit(
    val label: String,
    val detail: String,
    val activeCount: Int,
    val otherStates: String = ""
) {
    val summary: String
        get() {
            val activeText = if (activeCount == 1) "1 active" else "$activeCount active"
            val mixedText = if (otherStates.isBlank()) "" else "; also $otherStates"
            return "Audit: Channel: $label - $detail$mixedText; Device: not verified ($activeText)"
        }
}

data class NotificationAuditTrace(
    val activeNotificationCount: Int,
    val activePackageCount: Int,
    val activePackageSample: List<String>,
    val lastRefreshStatus: String
) {
    val summary: String
        get() {
            val sampleText = activePackageSample.ifEmpty { listOf("none") }.joinToString(separator = ", ")
            return "Trace: $activeNotificationCount active notifications / $activePackageCount packages; sample: $sampleText; $lastRefreshStatus"
        }
}

object NotificationAuditState {
    private const val MAX_TRACE_PACKAGE_SAMPLE = 5
    private val lock = Any()
    private var auditsByPackage: Map<String, AppNotificationAudit> = emptyMap()
    private var trace = NotificationAuditTrace(
        activeNotificationCount = 0,
        activePackageCount = 0,
        activePackageSample = emptyList(),
        lastRefreshStatus = "not refreshed"
    )
    private var hasSnapshot = false
    private var listenerConnected = false

    fun snapshot(): Map<String, AppNotificationAudit> {
        return synchronized(lock) {
            auditsByPackage.toMap()
        }
    }

    fun trace(): NotificationAuditTrace {
        return synchronized(lock) {
            trace.copy(activePackageSample = trace.activePackageSample.toList())
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

    fun clear(lastRefreshStatus: String = "cleared") {
        synchronized(lock) {
            auditsByPackage = emptyMap()
            trace = NotificationAuditTrace(
                activeNotificationCount = 0,
                activePackageCount = 0,
                activePackageSample = emptyList(),
                lastRefreshStatus = lastRefreshStatus
            )
            hasSnapshot = false
        }
    }

    fun replace(
        nextAudits: Map<String, AppNotificationAudit>,
        activeNotificationCount: Int,
        activePackages: Set<String>
    ) {
        synchronized(lock) {
            val sortedPackages = activePackages.sorted()
            auditsByPackage = nextAudits.toMap()
            trace = NotificationAuditTrace(
                activeNotificationCount = activeNotificationCount,
                activePackageCount = sortedPackages.size,
                activePackageSample = sortedPackages.take(MAX_TRACE_PACKAGE_SAMPLE),
                lastRefreshStatus = "last refresh ok"
            )
            hasSnapshot = true
        }
    }
}
