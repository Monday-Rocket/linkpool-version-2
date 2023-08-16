package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkResponse

interface LinkUserQuery {
    suspend fun getUnclassifiedLinks(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>

}