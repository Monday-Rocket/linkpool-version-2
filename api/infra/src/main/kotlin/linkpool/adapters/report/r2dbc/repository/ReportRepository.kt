package linkpool.adapters.report.r2dbc.repository

import linkpool.adapters.report.r2dbc.entity.ReportR2dbcEntity
import linkpool.report.model.ReportTargetType
import org.springframework.data.repository.reactive.ReactiveCrudRepository

fun ReportRepository.getById(id: Long) = findById(id)
    ?: throw NoSuchElementException("신고가 존재하지 않습니다. id: $id")

interface ReportRepository : ReactiveCrudRepository<ReportR2dbcEntity, Long> {
    fun findByReporterIdAndTargetTypeAndTargetId(reporterId: Long, targetType: ReportTargetType, targetId: Long): ReportR2dbcEntity?
}