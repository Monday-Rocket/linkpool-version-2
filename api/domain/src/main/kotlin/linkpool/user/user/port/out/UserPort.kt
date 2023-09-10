package linkpool.user.user.port.out

import linkpool.user.user.model.User

interface UserPort {
    suspend fun save(user: User): User
    suspend fun findById(id: Long): User?
    suspend fun findByUid(uid: String): User?
    suspend fun findByUidIncludingDeleted(uid: String): User?
    suspend fun existsByNickname(nickname: String): Boolean
    suspend fun patch(user: User)
}
