package linkpool.event

import linkpool.user.user.model.UserSignedOutEvent
import linkpool.user.user.port.out.UserEventPublishPort
import org.springframework.integration.annotation.Publisher
import org.springframework.integration.config.EnablePublisher
import org.springframework.stereotype.Component

@Component
@EnablePublisher
class UserSignedOutEventPublisher : UserEventPublishPort {

    @Publisher(channel = "signedOutEvent")
    override suspend fun publishUserSignedOutEvent(userSignedOutEvent: UserSignedOutEvent): UserSignedOutEvent {
        return userSignedOutEvent
    }
}