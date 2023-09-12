package linkpool.link.folder.service

import linkpool.common.DomainComponent
import linkpool.link.folder.port.`in`.FolderEventListener
import linkpool.link.folder.port.out.FolderPort
import linkpool.user.user.model.UserSignedOutEvent

@DomainComponent
class FolderEventListenerService(
    private val folderPort: FolderPort
) : FolderEventListener {
    override suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent) {
        return folderPort.softDeleteAll(userSignedOutEvent.userId)
    }
}