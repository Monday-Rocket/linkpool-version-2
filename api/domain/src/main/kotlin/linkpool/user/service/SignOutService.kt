package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.user.model.User
import linkpool.user.model.UserSignedOutEvent
import linkpool.user.port.`in`.SignOutUseCase
import linkpool.user.port.out.UserEventPublishPort
import linkpool.user.port.out.UserPort

@DomainComponent
class SignOutService(
    private val userPort: UserPort,
    private val userEventPublishPort: UserEventPublishPort
): SignOutUseCase {
    override suspend fun signOut(user: User) {
        user.delete()
        userPort.patch(user)
        userEventPublishPort.publishUserSignedOutEvent(UserSignedOutEvent(user.id))
    }
}