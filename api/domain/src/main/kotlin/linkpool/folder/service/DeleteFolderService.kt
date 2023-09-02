package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.folder.port.`in`.DeleteFolderUseCase
import linkpool.folder.port.out.FolderPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class DeleteFolderService(
  private val folderPort: FolderPort,
) : DeleteFolderUseCase {

  override suspend fun delete(userId: Long, folderId: Long) {
    val folder = folderPort.getById(folderId)

    if(userId != folder.userId) throw NotAuthorizedForDataException()

    folderPort.softDelete(folderId)
  }
}