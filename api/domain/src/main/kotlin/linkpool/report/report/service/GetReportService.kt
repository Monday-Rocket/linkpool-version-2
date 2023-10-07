package linkpool.report.report.service

import linkpool.common.DomainComponent
import linkpool.report.report.model.Report
import linkpool.report.report.model.ReportTarget
import linkpool.report.report.port.`in`.GetReportUseCase
import linkpool.report.report.port.out.ReportPort
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class ReportQueryService(
    private val reportPort: ReportPort,
): GetReportUseCase {
    override suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report? {
        return reportPort.findByReporterIdAndTarget(reporterId, target)
    }
}