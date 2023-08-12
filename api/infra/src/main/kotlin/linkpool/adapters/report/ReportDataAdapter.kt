package linkpool.adapters.report

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.adapters.report.r2dbc.entity.*
import linkpool.adapters.report.r2dbc.repository.ReportRepository
import linkpool.report.model.Report
import linkpool.report.model.ReportReason
import linkpool.report.model.ReportTarget
import linkpool.report.model.ReportTargetType
import linkpool.report.port.out.ReportPort
import org.springframework.data.relational.core.mapping.Column
import org.springframework.stereotype.Service


@Service
class ReportDataAdapter(
    private val reportRepository: ReportRepository
) : ReportPort {
    override suspend fun findById(id: Long): Report {
        return toModel(reportRepository.findById(id).awaitSingle())
    }

    override suspend fun save(report: Report): Report {
        return toModel(reportRepository.save(toJpa(report)).awaitSingle())
    }

    override suspend fun findByReporterIdAndTarget(reporterId: Long, target: ReportTarget): Report? {
        return reportRepository.findByReporterIdAndTargetTypeAndTargetId(reporterId, target.targetType, target.targetId)?.let { toModel(it) }
    }


    private fun toModel(entity: ReportR2dbcEntity) =
        Report(
            id = entity.id,
            reporterId = entity.reporterId,
            reason = toModel(
                ReportReasonJpaValue(
                    entity.reason,
                    entity.otherReason
                )
            ),
            target = toModel(
                ReportTargetJpaValue(
                    entity.targetType,
                    entity.targetId
                )
            ),
            createdDateTime = entity.createdDateTime
        )

    private fun toModel(value: ReportReasonJpaValue) =
        ReportReason(
            reason = value.reason,
            otherReason = value.otherReason
        )
    private fun toModel(value: ReportTargetJpaValue) =
        ReportTarget(
            targetType = value.targetType,
            targetId = value.targetId
        )

    private fun toJpa(model: Report) =
        ReportR2dbcEntity(
            id = model.id,
            reporterId = model.reporterId,
            reason = model.reason.reason,
            otherReason = model.reason.otherReason,
            targetType = model.target.targetType,
            targetId = model.target.targetId,
            createdDateTime = model.createdDateTime
        )

    private fun toJpa(model: ReportReason) =
        ReportReasonJpaValue(
            reason = model.reason,
            otherReason = model.otherReason
        )

    private fun toJpa(model: ReportTarget) =
        ReportTargetJpaValue(
            targetType = model.targetType,
            targetId = model.targetId
        )

}