package linkpool.user.port.out

import linkpool.user.model.User

interface UserPort {
    suspend fun save(user: User): User
    suspend fun findById(id: Long): User?
    suspend fun findByUid(uid: String): User?
    suspend fun findByUidIncludingDeleted(uid: String): User?
    suspend fun existsByInfoNickname(nickname: String): Boolean
    suspend fun patch(user: User)
}
