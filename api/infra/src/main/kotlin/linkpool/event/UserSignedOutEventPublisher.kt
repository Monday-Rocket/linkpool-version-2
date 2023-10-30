package linkpool.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import linkpool.user.user.model.UserSignedOutEvent
import linkpool.user.user.port.out.UserEventPublishPort
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.integration.config.EnablePublisher
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component


@Component
@EnablePublisher
class UserSignedOutEventPublisher(
    @Qualifier("signedOutEvent")
    private val outputChannel: MessageChannel
) : UserEventPublishPort {

    override fun publishUserSignedOutEvent(userSignedOutEvent: UserSignedOutEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = MessageBuilder.withPayload(userSignedOutEvent).build()
            outputChannel.send(message)
        }
    }
}