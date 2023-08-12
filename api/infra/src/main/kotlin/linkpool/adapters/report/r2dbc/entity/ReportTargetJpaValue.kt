package linkpool.adapters.report.r2dbc.entity

import linkpool.report.model.ReportTargetType

data class ReportTargetJpaValue (
    var targetType: ReportTargetType,
    var targetId: Long,
)
