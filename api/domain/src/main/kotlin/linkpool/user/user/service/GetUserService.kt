package linkpool.user.user.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.exception.NotSignedUpException
import linkpool.user.user.model.User
import linkpool.user.user.port.`in`.GetUserUseCase
import linkpool.user.user.port.out.UserPort
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class GetUserService(
    private val userPort: UserPort
): GetUserUseCase {
    override suspend fun getById(id: Long): User {
        return userPort.findById(id)?.also { user ->
            if (!user.signedUp()) throw NotSignedUpException()
        } ?: throw DataNotFoundException("회원이 존재하지 않습니다. id: $id")
    }
    override suspend fun getByIdOrNull(id: Long): User? {
        return userPort.findById(id)?.also { user ->
            if (!user.signedUp()) throw NotSignedUpException()
        }
    }
    override suspend fun existsByNickname(nickname: String): Boolean {
        return userPort.existsByNickname(nickname)
    }
}