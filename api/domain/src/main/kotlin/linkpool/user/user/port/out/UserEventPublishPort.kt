package linkpool.user.user.port.out

import linkpool.user.user.model.UserSignedOutEvent


interface UserEventPublishPort {
    fun publishUserSignedOutEvent(userSignedOutEvent: UserSignedOutEvent)
}