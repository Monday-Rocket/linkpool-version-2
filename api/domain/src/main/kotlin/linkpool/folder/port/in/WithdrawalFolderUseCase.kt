package linkpool.folder.port.`in`

import linkpool.user.model.UserSignedOutEvent

interface WithdrawalFolderUseCase {
  suspend fun deleteAll(event: UserSignedOutEvent)
}