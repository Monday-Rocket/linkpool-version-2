package linkpool.report.report.port.`in`

import linkpool.report.report.model.ReportReasonType
import linkpool.report.report.model.ReportTargetType

data class CreateReportRequest(
    val targetType: ReportTargetType,
    val targetId: Long,
    val reasonType: ReportReasonType,
    val otherReason: String? = null,
)

