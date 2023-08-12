package linkpool.adapters.report.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.common.rest.ApiResponse
import linkpool.report.port.`in`.CreateReportRequest
import linkpool.report.port.`in`.CreateReportUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/reports")
@RestController
class ReportRestController(
    private val createReportUseCase: CreateReportUseCase
) {
    @PostMapping
    suspend fun report(
        @RequestBody request: CreateReportRequest,
    ): ResponseEntity<ApiResponse<Unit>> {

        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name

        createReportUseCase.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}