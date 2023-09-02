package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkResponse

interface LinkUserQuery {
    suspend fun getUnclassifiedLinks(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>

}