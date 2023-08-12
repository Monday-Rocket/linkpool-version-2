package linkpool.report.port.`in`

import linkpool.report.model.ReportReasonType
import linkpool.report.model.ReportTargetType

data class CreateReportRequest(
    val targetType: ReportTargetType,
    val targetId: Long,
    val reasonType: ReportReasonType,
    val otherReason: String? = null,
)

