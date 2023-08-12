package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.user.model.User
import linkpool.user.port.`in`.SignOutUseCase
import linkpool.user.port.out.UserPort

@DomainComponent
class SignOutService(
    private val userPort: UserPort,
): SignOutUseCase {
    override suspend fun signOut(user: User) {
        // applicationEventPublisher.publishEvent(UserSignedOutEvent(user.id))
        user.delete()
        userPort.patch(user)
    }
}