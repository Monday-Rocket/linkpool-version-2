package linkpool.report.adapters.report.`in`.rest

import linkpool.common.rest.ApiResponse
import linkpool.report.report.port.`in`.CreateReportRequest
import linkpool.report.report.port.`in`.CreateReportUseCase
import linkpool.security.getPrincipal
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class ReportHandler(
    private val createReportUseCase: CreateReportUseCase
) {
  suspend fun report(request: ServerRequest): ServerResponse{
    val principal = getPrincipal()
    val reportRequest = request.awaitBody<CreateReportRequest>()

    createReportUseCase.create(principal.id, reportRequest)

    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
  }


}