package linkpool.report.port.out

import linkpool.report.model.Report
import linkpool.report.model.ReportTarget

interface ReportPort {
    suspend fun findById(id: Long): Report?
    suspend fun findByReporterIdAndTarget(reporterId: Long, target: ReportTarget): Report?
    suspend fun save(report: Report): Report
}