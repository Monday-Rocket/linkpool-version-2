package linkpool.report.port.`in`

import linkpool.report.model.Report
import linkpool.report.model.ReportTarget

interface GetReportUseCase {
    suspend fun getByReportIdAndTargetOrNull(reporterId: Long, target: ReportTarget): Report?
}