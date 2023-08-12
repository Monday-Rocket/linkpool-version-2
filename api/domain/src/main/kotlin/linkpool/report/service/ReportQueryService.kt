package linkpool.report.service

import linkpool.common.DomainComponent
import linkpool.report.model.Report
import linkpool.report.model.ReportTarget
import linkpool.report.port.`in`.ReportQuery
import linkpool.report.port.out.ReportPort

@DomainComponent
class ReportQueryService(
    private val reportPort: ReportPort,
): ReportQuery {
    override suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report? {
        return reportPort.findByReporterIdAndTarget(reporterId, target)
    }
}