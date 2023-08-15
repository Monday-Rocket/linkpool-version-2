package linkpool.user.port.out

import linkpool.user.model.UserSignedOutEvent

interface UserEventPublishPort {
    suspend fun publishUserSignedOutEvent(message: UserSignedOutEvent): UserSignedOutEvent
}