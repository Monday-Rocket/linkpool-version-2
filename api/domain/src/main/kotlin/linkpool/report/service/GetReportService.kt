package linkpool.report.service

import linkpool.common.DomainComponent
import linkpool.report.model.Report
import linkpool.report.model.ReportTarget
import linkpool.report.port.`in`.GetReportUseCase
import linkpool.report.port.out.ReportPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class ReportQueryService(
    private val reportPort: ReportPort,
): GetReportUseCase {
    override suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report? {
        return reportPort.findByReporterIdAndTarget(reporterId, target)
    }
}