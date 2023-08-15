package linkpool.user.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.exception.NotSignedUpException
import linkpool.user.model.User
import linkpool.user.port.`in`.GetUserUseCase
import linkpool.user.port.out.UserPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetUserService(
    private val userPort: UserPort
): GetUserUseCase {
    override suspend fun getById(id: Long): User {
        return userPort.findById(id)?.also { user ->
            if (!user.checkInfoCreated()) throw NotSignedUpException()
        } ?: throw DataNotFoundException("회원이 존재하지 않습니다. id: $id")
    }
    override suspend fun getByIdOrNull(id: Long): User? {
        return userPort.findById(id)?.also { user ->
            if (!user.checkInfoCreated()) throw NotSignedUpException()
        }
    }
    override suspend fun getByUid(uid: String): User {
        return userPort.findByUid(uid) ?.also { user ->
            if (!user.checkInfoCreated()) throw NotSignedUpException()
        } ?: throw DataNotFoundException("회원이 존재하지 않습니다. uid: $uid")
    }
    override suspend fun getByUidOrNull(uid: String): User? {
        return userPort.findByUid(uid) ?.also { user ->
            if (!user.checkInfoCreated()) throw NotSignedUpException()
        }
    }
    override suspend fun existsByNickname(nickname: String): Boolean {
        return userPort.existsByInfoNickname(nickname)
    }
}