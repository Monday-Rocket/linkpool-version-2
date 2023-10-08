package linkpool.report.report.service

import linkpool.common.DomainComponent
import linkpool.report.report.model.Report
import linkpool.report.report.model.ReportTarget
import linkpool.report.report.port.`in`.GetReportUseCase
import linkpool.report.report.port.out.ReportPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetReportService(
    private val reportPort: ReportPort,
): GetReportUseCase {
    override suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report? {
        return reportPort.findByReporterIdAndTarget(reporterId, target)
    }
}