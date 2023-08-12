package linkpool.link.port.`in`

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest

interface GetLinksUseCase {
	suspend fun getByUserId(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>
	suspend fun getByFolderId(folderId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse>
}