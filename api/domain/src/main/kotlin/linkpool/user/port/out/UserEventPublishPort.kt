package linkpool.user.port.out

import linkpool.user.model.UserSignedOutEvent

interface UserEventPublishPort {
    fun publishUserSignedOutEvent(userSignedOutEvent: UserSignedOutEvent)
}