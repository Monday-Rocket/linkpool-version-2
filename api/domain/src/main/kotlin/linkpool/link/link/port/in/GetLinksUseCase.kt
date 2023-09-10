package linkpool.link.link.port.`in`

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.link.model.Link

interface GetLinksUseCase {
	suspend fun getByCreatorId(creatorId: Long, paging: LinkPoolPageRequest): LinkPoolPage<Link>

}