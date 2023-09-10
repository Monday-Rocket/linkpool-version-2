package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateNicknameException
import linkpool.user.port.`in`.GetUserUseCase
import linkpool.user.port.`in`.UpdateUserUseCase
import linkpool.user.port.`in`.UserInfoRequest
import linkpool.user.port.out.UserPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateUserService(
    private val userPort: UserPort,
    private val getUserUseCase: GetUserUseCase
) : UpdateUserUseCase {
    override suspend fun updateUserInfo(userId: Long, userInfoRequest: UserInfoRequest) {
        val user = getUserUseCase.getById(userId)
        userInfoRequest.nickname ?.let {
            require(!userPort.existsByInfoNickname(userInfoRequest.nickname)) {
                throw DuplicateNicknameException()
            }
        }
        user.patchInfo(userInfoRequest)
        userPort.patch(user)
    }
}