package linkpool.report.fixtures

import linkpool.report.model.*
import linkpool.report.report.model.*
import linkpool.report.report.port.`in`.CreateReportRequest
import linkpool.report2.report.model.*
import java.time.LocalDateTime

const val REPORT_OTHER_REASON: String = "기타 사유"

fun createReport(
    id: Long = 1L,
    reportId: Long = 1L,
    reason: ReportReasonType = ReportReasonType.COMMERCIAL,
    otherReason: String? = REPORT_OTHER_REASON,
    targetType: ReportTargetType = ReportTargetType.USER,
    targetId: Long = 1L,
    createdDateTime: LocalDateTime = LocalDateTime.now()
): Report {
    return Report(
        id = id,
        reporterId = reportId,
        reason = ReportReason(
            reason = reason,
            otherReason = otherReason
        ),
        target = ReportTarget(
            targetType = targetType,
            targetId = targetId
        ),
        createdDateTime = createdDateTime,
    )
}

fun createCreateReportRequest(
    targetType: ReportTargetType = ReportTargetType.USER,
    targetId: Long = 1L,
    reasonType: ReportReasonType = ReportReasonType.COMMERCIAL,
    otherReason: String? = REPORT_OTHER_REASON,
): CreateReportRequest {
    return CreateReportRequest(
        targetType = targetType,
        targetId = targetId,
        reasonType = reasonType,
        otherReason = otherReason
    )
}

fun createReportTarget(
    targetType: ReportTargetType = ReportTargetType.USER,
    targetId: Long = 1L,
): ReportTarget {
    return ReportTarget(
        targetType = targetType,
        targetId = targetId
    )
}