package linkpool.report.model

data class ReportReason(
    var reason: ReportReasonType,
    var otherReason: String? = null,
)