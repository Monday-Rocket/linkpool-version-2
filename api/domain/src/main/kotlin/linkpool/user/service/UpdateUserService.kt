package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateNicknameException
import linkpool.user.model.User
import linkpool.user.port.`in`.UpdateUserUseCase
import linkpool.user.port.`in`.UserInfoRequest
import linkpool.user.port.out.UserPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateUserService(
    private val userPort: UserPort,
) : UpdateUserUseCase {
    override suspend fun updateUserInfo(user: User, userInfoRequest: UserInfoRequest) {
        userInfoRequest.nickname ?.let {
            require(!userPort.existsByInfoNickname(userInfoRequest.nickname)) {
                throw DuplicateNicknameException()
            }
        }
        user.patchInfo(userInfoRequest)
        userPort.patch(user)
    }
}