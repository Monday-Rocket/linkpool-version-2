package linkpool.event

import kotlinx.coroutines.*
import linkpool.common.log.kLogger
import linkpool.link.folder.port.`in`.FolderEventListener
import linkpool.link.link.port.`in`.LinkEventListener
import linkpool.user.user.model.UserSignedOutEvent
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

fun <T> CoroutineScope.safeAsync(block: suspend () -> T): Deferred<Result<T>> {
    return async {
        runCatching {
            block()
        }
    }
}

@Component
class UserSignedOutEventListener(
    private val folderEventListener: FolderEventListener,
    private val linkEventListener: LinkEventListener
) {

    private val log = kLogger()

    @ServiceActivator(inputChannel = "signedOutEvent")
    fun handleForSignedOutEventListener(@Payload message: Message<UserSignedOutEvent>) {
        CoroutineScope(CoroutineExceptionHandler { _, exception ->
            log.error("$exception in the handleForSignedOutEventListener")
        } + Dispatchers.IO).launch {
            val folderEventDeferred = safeAsync {
                folderEventListener.deleteBatchAll(message.payload)
            }
            val linkEventDeferred = safeAsync {
                linkEventListener.deleteBatchAll(message.payload)
            }

            folderEventDeferred.await()
                .onSuccess { log.info("succeed in folderEventListener from UserSignedOut... userId: ${message.payload.userId}") }
                .onFailure { e -> log.error("Error in folderEventListener from UserSignedOut... userId: ${message.payload.userId} \n error: ${e.message}") }
            linkEventDeferred.await()
                .onSuccess { log.info("succeed in linkEventListener from UserSignedOut... userId: ${message.payload.userId}") }
                .onFailure { e -> log.error("Error in linkEventListener from UserSignedOut... userId: ${message.payload.userId} \n error: ${e.message}") }

        }
    }
}