package linkpool.user.user.service

import linkpool.common.DomainComponent
import linkpool.user.user.port.`in`.GetUserUseCase

import linkpool.user.user.port.`in`.SignOutUseCase
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.port.out.UserEventPublishPort
import linkpool.user.user.model.UserSignedOutEvent

import javax.transaction.Transactional

@DomainComponent
@Transactional
class SignOutService(
    private val getUserUseCase: GetUserUseCase,
    private val userPort: UserPort,
    private val userEventPublishPort: UserEventPublishPort
): SignOutUseCase {

    override suspend fun signOut(userId: Long) {
        val user = getUserUseCase.getById(userId)
        user.signOut()
        userPort.patch(user)
        userEventPublishPort.publishUserSignedOutEvent(UserSignedOutEvent(user.id))
    }
}