package linkpool.user.user.port.out

import linkpool.user.user.model.UserSignedOutEvent


interface UserEventPublishPort {
    suspend fun publishUserSignedOutEvent(message: UserSignedOutEvent): UserSignedOutEvent
}