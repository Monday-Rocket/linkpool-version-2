package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkWithUserResult

interface LinkUserQuery {
    suspend fun getPageOfMyFolder(ownerId: Long, folderId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult>
    suspend fun getUnclassifiedLinks(ownerId: Long, linkPoolPageRequest: LinkPoolPageRequest): Any
}