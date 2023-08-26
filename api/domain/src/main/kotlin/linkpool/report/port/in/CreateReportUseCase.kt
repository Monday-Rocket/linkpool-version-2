package linkpool.report.port.`in`


interface CreateReportUseCase {
    suspend fun create(userId: Long, request: CreateReportRequest)
}