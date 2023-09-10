package linkpool.link.folder.service

import linkpool.common.DomainComponent
import linkpool.link.folder.port.`in`.WithdrawalFolderUseCase
import linkpool.link.folder.port.out.FolderPort
import linkpool.user.user.model.UserSignedOutEvent
import javax.transaction.Transactional

@DomainComponent
@Transactional
class WithdrawalFolderService(
  private val folderPort: FolderPort,
) : WithdrawalFolderUseCase {
  override suspend fun deleteAll(event: UserSignedOutEvent) {
    folderPort.softDeleteAll(event.userId)
  }
}