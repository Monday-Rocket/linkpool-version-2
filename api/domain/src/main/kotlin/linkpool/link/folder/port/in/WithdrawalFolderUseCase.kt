package linkpool.link.folder.port.`in`

import linkpool.user.user.model.UserSignedOutEvent

interface WithdrawalFolderUseCase {
  suspend fun deleteAll(event: UserSignedOutEvent)
}