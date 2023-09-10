package linkpool.folder.port.`in`

import linkpool.user.model.UserSignedOutEvent

interface FolderEventListener {
    suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent)
}