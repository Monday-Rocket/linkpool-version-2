package linkpool.link.port.`in`

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.model.Link

interface GetLinksUseCase {
	suspend fun getByUserId(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<Link>
	suspend fun getByFolderId(folderId: Long, paging: LinkPoolPageRequest): LinkPoolPage<Link>
}