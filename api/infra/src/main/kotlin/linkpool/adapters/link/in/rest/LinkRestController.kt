package linkpool.adapters.link.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.rest.ApiResponse
import linkpool.link.port.`in`.*
import linkpool.query.linkuser.LinkUserQuery
import linkpool.query.searchlink.SearchLinkQuery
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*

//@RequestMapping("/links")
//@RestController
class LinkRestController(
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val createLinkUseCase: CreateLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val updateLinkUseCase: UpdateLinkUseCase,
    private val linkUserQuery: LinkUserQuery,
    private val searchLinkQuery: SearchLinkQuery
) {
    @PostMapping
    suspend fun create(
        @RequestBody request: SaveLinkRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        createLinkUseCase.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @PatchMapping("/{linkId}")
    suspend fun update(
        @PathVariable("linkId") linkId: Long,
        @RequestBody request: UpdateLinkRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        updateLinkUseCase.update(uid, linkId, request)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @DeleteMapping("/{linkId}")
    suspend fun delete(
        @PathVariable("linkId") linkId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        deleteLinkUseCase.delete(uid, linkId)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @GetMapping
    suspend fun getByUserId(
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
    ): ResponseEntity<ApiResponse<LinkPoolPage<LinkResponse>>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        return ResponseEntity.ok(ApiResponse.success(getLinksUseCase.getByUserId(uid, LinkPoolPageRequest(pageNo, pageSize))))
    }

    @GetMapping("/unclassified")
    suspend fun getLinksOfFolder(
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
    ): ResponseEntity<ApiResponse<LinkPoolPage<LinkResponse>>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        return ResponseEntity.ok(ApiResponse.success(linkUserQuery.getUnclassifiedLinks(uid, LinkPoolPageRequest(pageNo, pageSize))))
    }

    @GetMapping("/search")
    suspend fun searchLinkByKeyword(
        @RequestParam(value = "my_links_only", required = true) myLinksOnly: Boolean,
        @RequestParam(value = "keyword", required = true) keyword: String,
        @RequestParam(value = "page_no", defaultValue = "0") pageNo: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
    ): ResponseEntity<ApiResponse<LinkPoolPage<LinkWithUserResponse>>> {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name

        return ResponseEntity.ok(ApiResponse.success(searchLinkQuery.searchByKeyword(uid, keyword, LinkPoolPageRequest(pageNo, pageSize))))
    }


}