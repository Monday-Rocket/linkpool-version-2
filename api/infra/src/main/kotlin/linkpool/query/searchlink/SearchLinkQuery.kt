package linkpool.query.searchlink

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.port.`in`.LinkWithUserResponse

interface SearchLinkQuery {
    suspend fun searchByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse>
    suspend fun searchMyLinkByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse>
}