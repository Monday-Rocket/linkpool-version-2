package linkpool.user.user.service

import linkpool.common.DomainComponent
import linkpool.exception.DuplicateNicknameException
import linkpool.user.user.port.`in`.GetUserUseCase
import linkpool.user.user.port.`in`.UpdateUserUseCase
import linkpool.user.user.port.`in`.ProfileRequest
import linkpool.user.user.port.out.UserPort
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class UpdateUserService(
    private val userPort: UserPort,
    private val getUserUseCase: GetUserUseCase
) : UpdateUserUseCase {
    override suspend fun updateProfile(userId: Long, profileRequest: ProfileRequest) {
        val user = getUserUseCase.getById(userId)
        profileRequest.nickname ?.let {
            require(!userPort.existsByNickname(profileRequest.nickname)) {
                throw DuplicateNicknameException()
            }
        }
        user.updateProfile(profileRequest)
        userPort.patch(user)
    }
}