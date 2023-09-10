package linkpool.user2.user.service

import linkpool.common.DomainComponent
import linkpool.user2.user.port.`in`.GetUserUseCase
import linkpool.user2.user.port.`in`.SignOutUseCase
import linkpool.user2.user.port.out.UserPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class SignOutService(
    private val getUserUseCase: GetUserUseCase,
    private val userPort: UserPort,
): SignOutUseCase {
    override suspend fun signOut(userId: Long) {
        val user = getUserUseCase.getById(userId)
        // applicationEventPublisher.publishEvent(UserSignedOutEvent(user.id))
        user.signOut()
        userPort.patch(user)
    }
}