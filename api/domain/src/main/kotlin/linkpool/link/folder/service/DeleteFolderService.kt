package linkpool.link.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.folder.port.`in`.DeleteFolderUseCase
import linkpool.link.folder.port.out.FolderPort
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class DeleteFolderService(
  private val folderPort: FolderPort,
) : DeleteFolderUseCase {

  override suspend fun delete(ownerId: Long, folderId: Long) {
    val folder = folderPort.getById(folderId)

    if(ownerId != folder.ownerId) throw NotAuthorizedForDataException()

    folderPort.softDelete(folderId)
  }
}