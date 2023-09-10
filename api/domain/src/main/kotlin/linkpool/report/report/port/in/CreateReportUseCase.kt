package linkpool.report.report.port.`in`


interface CreateReportUseCase {
    suspend fun create(userId: Long, request: CreateReportRequest)
}