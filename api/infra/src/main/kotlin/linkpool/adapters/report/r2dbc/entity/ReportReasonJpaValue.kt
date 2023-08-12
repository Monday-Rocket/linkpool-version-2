package linkpool.adapters.report.r2dbc.entity

import linkpool.report.model.ReportReasonType
data class ReportReasonJpaValue (
    var reason: ReportReasonType,
    var otherReason: String? = null,
)