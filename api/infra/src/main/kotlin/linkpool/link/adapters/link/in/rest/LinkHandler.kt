package linkpool.link.adapters.link.`in`.rest

import linkpool.LinkPoolPageRequest
import linkpool.common.rest.ApiResponse
import linkpool.link.link.port.`in`.*
import linkpool.query.linkuser.LinkUserQuery
import linkpool.query.linkuser.SearchLinkQuery
import linkpool.security.getPrincipal
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class LinkHandler(
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val createLinkUseCase: CreateLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val searchLinkQuery: SearchLinkQuery,
    private val updateLinkUseCase: UpdateLinkUseCase,
    private val linkUserQuery: LinkUserQuery,
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val saveLinkRequest = request.awaitBody<SaveLinkRequest>()

        createLinkUseCase.create(principal.id, saveLinkRequest)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val linkId = request.pathVariable("linkId").toLong()
        val updateLinkRequest = request.awaitBody<UpdateLinkRequest>()

        updateLinkUseCase.update(principal.id, linkId, updateLinkRequest)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val linkId = request.pathVariable("linkId").toLong()

        deleteLinkUseCase.delete(principal.id, linkId)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))

    }

    suspend fun getByCreatorId(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val pageNo = request.queryParam("page_no").get().toInt()
        val pageSize = request.queryParam("page_size").get().toInt()

        val links = getLinksUseCase.getByCreatorId(principal.id, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
    }

    suspend fun getLinksOfFolder(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val pageNo = request.queryParam("page_no").get().toInt()
        val pageSize = request.queryParam("page_size").get().toInt()
        val folderId = request.pathVariable("folderId").toLong()

        val links = linkUserQuery.getPageOfMyFolder(principal.id, folderId, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
    }

    suspend fun getMyUnclassifiedLinks(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val pageNo = request.queryParam("page_no").get().toInt()
        val pageSize = request.queryParam("page_size").get().toInt()

        val links = linkUserQuery.getUnclassifiedLinks(principal.id, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
    }
    suspend fun searchLinkByKeyword(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val keyword = request.queryParam("keyword").get()
        val pageNo = request.queryParam("page_no").get().toInt()
        val pageSize = request.queryParam("page_size").get().toInt()

        val links = searchLinkQuery.searchByKeyword(principal.id, keyword, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
    }

    suspend fun searchMyLinkByKeyword(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val keyword = request.queryParam("keyword").get()
        val pageNo = request.queryParam("page_no").get().toInt()
        val pageSize = request.queryParam("page_size").get().toInt()

        val links = searchLinkQuery.searchMyLinkByKeyword(principal.id, keyword, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
    }
}