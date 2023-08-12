package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.user.model.User
import linkpool.user.port.`in`.CreateUserResponse
import linkpool.user.port.`in`.CreateUserUseCase
import linkpool.user.port.out.UserPort

@DomainComponent
class CreateUserService(
    private val userPort: UserPort,
): CreateUserUseCase {

    override suspend fun createUser(uid: String): CreateUserResponse {
        val user = userPort.findByUidIncludingDeleted(uid) ?.apply {
            if (this.isNotActivated()) {
                this.activate()
                userPort.patch(this)
            }
        } ?: userPort.save(User(uid = uid))
        return CreateUserResponse(id = user.id, isNew = !user.signedUp())
    }

}