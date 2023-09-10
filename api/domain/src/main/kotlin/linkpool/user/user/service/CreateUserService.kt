package linkpool.user2.user.service

import linkpool.common.DomainComponent
import linkpool.user2.user.model.User
import linkpool.user2.user.port.`in`.CreateUserResponse
import linkpool.user2.user.port.`in`.CreateUserUseCase
import linkpool.user2.user.port.out.UserAuthPort
import linkpool.user2.user.port.out.UserPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class CreateUserService(
    private val userPort: UserPort,
    private val authPort: UserAuthPort
): CreateUserUseCase {

    override suspend fun createUser(uid: String): CreateUserResponse {
        val user = userPort.findByUidIncludingDeleted(uid) ?.apply {
            if (this.isNotActivated()) {
                this.activate()
                userPort.patch(this)
            }
        } ?: userPort.save(User(uid = uid)).also { user ->
                authPort.setUserId(user.uid, user.id)
        }

        return CreateUserResponse(id = user.id, isNew = !user.signedUp())
    }

}