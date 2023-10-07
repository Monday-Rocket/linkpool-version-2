package linkpool.link.folder.port.`in`

import linkpool.user.user.model.UserSignedOutEvent


interface FolderEventListener {
    suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent)
}