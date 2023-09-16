package linkpool.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import linkpool.user.model.UserSignedOutEvent
import linkpool.user.port.out.UserEventPublishPort
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.integration.config.EnablePublisher
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener


@Component
@EnablePublisher
class UserSignedOutEventPublisher(
    @Qualifier("signedOutEvent")
    private val outputChannel: MessageChannel
) : UserEventPublishPort {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun publishUserSignedOutEvent(userSignedOutEvent: UserSignedOutEvent) {
        CoroutineScope(Dispatchers.IO).run {
            val message = MessageBuilder.withPayload(userSignedOutEvent).build()
            outputChannel.send(message)
        }
    }
}