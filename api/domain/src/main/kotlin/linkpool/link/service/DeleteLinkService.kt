package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.port.`in`.DeleteLinkUseCase
import linkpool.link.port.out.LinkPort
import linkpool.link.port.out.getById
import javax.transaction.Transactional

@DomainComponent
@Transactional
class DeleteLinkService(
  private val linkPort: LinkPort,
  ): DeleteLinkUseCase {

  override suspend fun delete(userId: Long, linkId: Long) {
    val link = linkPort.getById(linkId)

    if (link.isSameUser(userId))
      throw NotAuthorizedForDataException()

    link.delete()
    linkPort.delete(link)
  }

  override suspend fun deleteByFolder(folderId: Long)
    = linkPort.deleteBatchByFolderId(folderId)
}