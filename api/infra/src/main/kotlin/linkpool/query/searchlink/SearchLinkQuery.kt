package linkpool.query.searchlink

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.port.`in`.LinkWithUserResponse

interface SearchLinkQuery {
    suspend fun searchByKeyword(uid: String, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse>
    suspend fun searchMyLinkByKeyword(uid: String, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse>
}