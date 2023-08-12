package linkpool.adapters.folderlink.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.common.rest.ApiResponse
import linkpool.folderlink.port.`in`.BulkCreateRequest
import linkpool.folderlink.port.`in`.CreateFolderLinkBulkUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/bulk")
@RestController
class BulkRestController(
    private val createFolderLinkBulkUseCase: CreateFolderLinkBulkUseCase
) {
    @PostMapping
    suspend fun create(
        @RequestBody request: BulkCreateRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name

        createFolderLinkBulkUseCase.createBulk(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}
