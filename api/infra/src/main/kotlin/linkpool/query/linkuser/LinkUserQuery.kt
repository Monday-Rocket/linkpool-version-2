package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.port.`in`.LinkResponse

interface LinkUserQuery {
    suspend fun getUnclassifiedLinks(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>

}