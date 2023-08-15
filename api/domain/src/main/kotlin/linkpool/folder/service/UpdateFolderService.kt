package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.folder.port.`in`.UpdateFolderRequest
import linkpool.folder.port.`in`.UpdateFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.user.port.`in`.GetUserUseCase
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateFolderService(
    private val getUserUseCase: GetUserUseCase,
    private val folderPort: FolderPort,
) : UpdateFolderUseCase {

  override suspend fun update(uid: String, folderId: Long, request: UpdateFolderRequest) {
    val user = getUserUseCase.getByUid(uid)
    val folder = folderPort.getById(folderId)

    if(!folder.isOwner(user.id)) {
      throw NotAuthorizedForDataException()
    }

    folderPort.update(folder.update(request))
  }
}