package linkpool.report.adapters.report.r2dbc.repository

import linkpool.report.adapters.report.r2dbc.entity.ReportR2dbcEntity
import linkpool.report.report.model.ReportTargetType
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReportRepository : ReactiveCrudRepository<ReportR2dbcEntity, Long> {
    fun findByReporterIdAndTargetTypeAndTargetId(reporterId: Long, targetType: ReportTargetType, targetId: Long): ReportR2dbcEntity?
}