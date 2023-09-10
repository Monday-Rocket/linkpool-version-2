package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkWithUserResult

interface LinkUserQuery {
    suspend fun getPageOfMyFolder(userId: Long, folderId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult>
    suspend fun getUnclassifiedLinks(id: Long, linkPoolPageRequest: LinkPoolPageRequest): Any
}