package linkpool.adapters.link.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPageRequest
import linkpool.common.rest.ApiResponse
import linkpool.link.port.`in`.*
import linkpool.query.linkuser.LinkUserQuery
import linkpool.query.searchlink.SearchLinkQuery
import org.springframework.security.core.context.ReactiveSecurityContextHolder
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
    private val linkUserQuery: LinkUserQuery
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()

        val uid = context.authentication.name
        val request = request.awaitBody<SaveLinkRequest>()

        createLinkUseCase.create(uid, request)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()

        val uid = context.authentication.name
        val linkId = request.pathVariable("linkId").toLong()
        val request = request.awaitBody<UpdateLinkRequest>()

        updateLinkUseCase.update(uid, linkId, request)

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()

        val uid = context.authentication.name
        val linkId = request.pathVariable("linkId").toLong()

        deleteLinkUseCase.delete(uid, linkId)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))

    }

    suspend fun getByUserId(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()

        val uid = context.authentication.name
        val pageNo = request.queryParam("pageNo").get().toInt()
        val pageSize = request.queryParam("pageSize").get().toInt()

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(getLinksUseCase.getByUserId(uid, LinkPoolPageRequest(pageNo, pageSize))))
    }

    suspend fun getLinksOfFolder(request: ServerRequest): ServerResponse{
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name
        val pageNo = request.queryParam("pageNo").get().toInt()
        val pageSize = request.queryParam("pageSize").get().toInt()


        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(linkUserQuery.getUnclassifiedLinks(uid, LinkPoolPageRequest(pageNo, pageSize))))
    }

    suspend fun searchLinkByKeyword(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val keyword = request.queryParam("keyword").get()
        val pageNo = request.queryParam("pageNo").get().toInt()
        val pageSize = request.queryParam("pageSize").get().toInt()

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(searchLinkQuery.searchByKeyword(uid, keyword, LinkPoolPageRequest(pageNo, pageSize))))
    }

    suspend fun searchMyLinkByKeyword(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val keyword = request.queryParam("keyword").get()
        val pageNo = request.queryParam("pageNo").get().toInt()
        val pageSize = request.queryParam("pageSize").get().toInt()

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(searchLinkQuery.searchMyLinkByKeyword(uid, keyword, LinkPoolPageRequest(pageNo, pageSize))))
    }
}