package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.folder.port.`in`.WithdrawalFolderUseCase
import linkpool.folder.port.out.FolderPort
import linkpool.user.model.UserSignedOutEvent

@DomainComponent
class WithdrawalFolderService(
  private val folderPort: FolderPort,
) : WithdrawalFolderUseCase {
  override suspend fun deleteAll(event: UserSignedOutEvent) {
    folderPort.softDeleteAll(event.userId)
  }
}