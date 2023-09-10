package linkpool.link.adapters.folderlink.`in`.rest

import linkpool.common.rest.ApiResponse
import linkpool.link.folderlink.port.`in`.BulkCreateRequest
import linkpool.link.folderlink.port.`in`.CreateFolderLinkBulkUseCase
import linkpool.security.getPrincipal
import org.springframework.http.ResponseEntity
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
        val principal = getPrincipal()

        createFolderLinkBulkUseCase.createBulk(principal.id, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}
