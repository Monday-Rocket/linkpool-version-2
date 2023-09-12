package linkpool.report.report.model

import java.time.LocalDateTime

class Report (
    val id: Long = 0L,
    var reporterId: Long,
    var reason: ReportReason,
    var target: ReportTarget,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
) {

    var deleted: Boolean = false
        private set

    fun delete() {
        deleted = true
    }
}
