package linkpool.report.report.port.out

import linkpool.report.report.model.Report
import linkpool.report.report.model.ReportTarget

interface ReportPort {
    suspend fun findById(id: Long): Report?
    suspend fun findByReporterIdAndTarget(reporterId: Long, target: ReportTarget): Report?
    suspend fun save(report: Report): Report
}