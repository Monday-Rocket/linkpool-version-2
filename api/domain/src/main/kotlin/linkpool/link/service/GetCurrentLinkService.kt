package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.link.model.Link
import linkpool.link.port.`in`.GetCurrentLinkUseCase
import linkpool.link.port.out.LinkPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetCurrentLinkService(
	private val linkPort: LinkPort
): GetCurrentLinkUseCase {
	override suspend fun getCurrentLinkByFolderId(folderId: Long): Link? =
		linkPort.findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId)
}