package linkpool.link.port.`in`

import linkpool.link.model.Link

interface GetCurrentLinkUseCase {
	suspend fun getCurrentLinkByFolderId(folderId: Long): Link?
	
}