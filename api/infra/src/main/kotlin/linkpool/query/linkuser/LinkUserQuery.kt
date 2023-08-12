package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.port.`in`.LinkResponse

interface LinkUserQuery {
    suspend fun getUnclassifiedLinks(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>

}