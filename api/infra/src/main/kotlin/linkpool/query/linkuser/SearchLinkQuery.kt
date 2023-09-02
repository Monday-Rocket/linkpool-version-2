package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkWithUserResult

interface SearchLinkQuery {
    suspend fun searchByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult>
    suspend fun searchMyLinkByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult>
}