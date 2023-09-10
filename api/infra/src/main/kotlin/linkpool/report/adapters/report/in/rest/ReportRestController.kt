package linkpool.report.adapters.report.`in`.rest

import linkpool.common.rest.ApiResponse
import linkpool.report.report.port.`in`.CreateReportRequest
import linkpool.report.report.port.`in`.CreateReportUseCase
import linkpool.security.getPrincipal
import org.springframework.http.ResponseEntity
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
        val principal = getPrincipal()

        createReportUseCase.create(principal.id, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}