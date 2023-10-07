package linkpool.report.report.port.`in`

import linkpool.report.report.model.Report
import linkpool.report.report.model.ReportTarget

interface GetReportUseCase {
    suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report?
}