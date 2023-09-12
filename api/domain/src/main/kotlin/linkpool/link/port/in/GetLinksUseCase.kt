package linkpool.link.port.`in`

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.model.Link

interface GetLinksUseCase {
	suspend fun getByUserId(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<Link>

}