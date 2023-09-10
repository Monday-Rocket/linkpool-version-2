package linkpool.folder.service

import linkpool.common.DomainComponent
import linkpool.folder.port.`in`.FolderEventListener
import linkpool.folder.port.out.FolderPort
import linkpool.user.model.UserSignedOutEvent

@DomainComponent
class FolderEventListenerService(
    private val folderPort: FolderPort
) : FolderEventListener {
    override suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent) {
        return folderPort.softDeleteAll(userSignedOutEvent.userId)
    }
}