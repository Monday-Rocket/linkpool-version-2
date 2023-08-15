package linkpool.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import linkpool.folder.port.`in`.FolderEventListener
import linkpool.link.port.`in`.LinkEventListener
import linkpool.user.model.UserSignedOutEvent
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserSignedOutEventListener(
    private val folderEventListener: FolderEventListener,
    private val linkEventListener: LinkEventListener
) {

    @ServiceActivator(inputChannel = "signedOutEvent")
    fun handleForSignedOutEventListener(@Payload message: Message<UserSignedOutEvent>) {
        CoroutineScope(Job()).launch {
            folderEventListener.deleteBatchAll(message.payload)
            linkEventListener.deleteBatchAll(message.payload)
        }
    }
}