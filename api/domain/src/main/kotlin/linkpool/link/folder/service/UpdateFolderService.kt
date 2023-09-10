package linkpool.link.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.folder.port.`in`.UpdateFolderRequest
import linkpool.link.folder.port.`in`.UpdateFolderUseCase
import linkpool.link.folder.port.out.FolderPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateFolderService(
  private val folderPort: FolderPort,
) : UpdateFolderUseCase {

  override suspend fun update(ownerId: Long, folderId: Long, request: UpdateFolderRequest) {
    val folder = folderPort.getById(folderId)

    if(!folder.isOwner(ownerId)) {
      throw NotAuthorizedForDataException()
    }

    folderPort.update(folder.update(request))
  }
}