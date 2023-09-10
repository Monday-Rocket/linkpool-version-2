package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.folder.port.`in`.UpdateFolderRequest
import linkpool.folder.port.`in`.UpdateFolderUseCase
import linkpool.folder.port.out.FolderPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateFolderService(
    private val folderPort: FolderPort,
) : UpdateFolderUseCase {

  override suspend fun update(userId: Long, folderId: Long, request: UpdateFolderRequest) {
    val folder = folderPort.getById(folderId)

    if(!folder.isOwner(userId)) {
      throw NotAuthorizedForDataException()
    }

    folderPort.update(folder.update(request))
  }
}