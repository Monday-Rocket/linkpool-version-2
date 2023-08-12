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
        val pageNo = request.pathVariable("pageNo").toInt()
        val pageSize = request.pathVariable("pageSize").toInt()
        getLinksUseCase.getByUserId(uid, LinkPoolPageRequest(pageNo, pageSize))
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun getLinksOfFolder(request: ServerRequest): ServerResponse{
        val context = ReactiveSecurityContextHolder
                .getContext()
                .awaitSingle()
        val uid = context.authentication.name
        val pageNo = request.pathVariable("pageNo").toInt()
        val pageSize = request.pathVariable("pageSize").toInt()

        linkUserQuery.getUnclassifiedLinks(uid, LinkPoolPageRequest(pageNo, pageSize))

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun searchLinkByKeyword(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val keyword = request.pathVariable("keyword")
        val pageNo = request.pathVariable("pageNo").toInt()
        val pageSize = request.pathVariable("pageSize").toInt()

        searchLinkQuery.searchByKeyword(uid, keyword, LinkPoolPageRequest(pageNo, pageSize))

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }

    suspend fun searchMyLinkByKeyword(request: ServerRequest): ServerResponse {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val keyword = request.pathVariable("keyword")
        val pageNo = request.pathVariable("pageNo").toInt()
        val pageSize = request.pathVariable("pageSize").toInt()

        searchLinkQuery.searchMyLinkByKeyword(uid, keyword, LinkPoolPageRequest(pageNo, pageSize))

        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(true))
    }
}