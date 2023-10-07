package linkpool.user.user.service

import linkpool.common.DomainComponent
import linkpool.user.user.port.`in`.GetUserUseCase

import linkpool.user.user.port.`in`.SignOutUseCase
import linkpool.user.user.port.out.UserPort
import linkpool.user.user.port.out.UserEventPublishPort
import linkpool.user.user.model.UserSignedOutEvent
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

@DomainComponent
@Transactional
class SignOutService(
    private val getUserUseCase: GetUserUseCase,
    private val userPort: UserPort,
    private val userEventPublishPort: UserEventPublishPort,
    private val transactionalOperator: TransactionalOperator
): SignOutUseCase {

    override suspend fun signOut(userId: Long) {
        transactionalOperator.executeAndAwait {
            val user = getUserUseCase.getById(userId)
            user.signOut()
            userPort.patch(user)
        }.run {
            userEventPublishPort.publishUserSignedOutEvent(UserSignedOutEvent(userId))
        }
    }
}