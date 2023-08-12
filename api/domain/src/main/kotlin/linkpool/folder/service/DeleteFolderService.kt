package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.folder.port.`in`.DeleteFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.user.port.`in`.GetUserUseCase

@DomainComponent
class DeleteFolderService(
  private val folderPort: FolderPort,
  private val userUseCase: GetUserUseCase
) : DeleteFolderUseCase {

  override suspend fun delete(uid: String, folderId: Long) {
    val user = userUseCase.getByUid(uid)
    val folder = folderPort.getById(folderId)

    if(user.id != folder.userId) throw NotAuthorizedForDataException()

    folderPort.softDelete(folderId)
  }
}