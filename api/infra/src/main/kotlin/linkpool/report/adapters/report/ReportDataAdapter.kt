package linkpool.report.adapters.report

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.report.adapters.report.r2dbc.repository.ReportRepository
import linkpool.report.adapters.report.r2dbc.entity.ReportR2dbcEntity
import linkpool.report.report.model.Report
import linkpool.report.report.model.ReportReason
import linkpool.report.report.model.ReportTarget
import linkpool.report.report.model.ReportTargetType
import linkpool.report.report.port.out.ReportPort
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
            reason = ReportReason(
                entity.reason,
                entity.otherReason
            ),
            target = ReportTarget(
                when(entity.targetType){
                    0 -> ReportTargetType.USER
                    1 -> ReportTargetType.LINK
                    else -> ReportTargetType.USER
                },
                entity.targetId
            ),
            createdDateTime = entity.createdDateTime
        )


    private fun toJpa(model: Report) =
        ReportR2dbcEntity(
            id = model.id,
            reporterId = model.reporterId,
            reason = model.reason.reason,
            otherReason = model.reason.otherReason,
            targetType = model.target.targetType.ordinal,
            targetId = model.target.targetId,
            createdDateTime = model.createdDateTime
        )

}