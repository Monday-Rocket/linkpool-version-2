package linkpool.report.port.`in`


interface CreateReportUseCase {
    suspend fun create(uid: String, request: CreateReportRequest)
}