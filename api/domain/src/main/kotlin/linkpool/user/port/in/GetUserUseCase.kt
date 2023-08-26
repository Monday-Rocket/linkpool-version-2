package linkpool.user.port.`in`

import linkpool.user.model.User

interface GetUserUseCase {
    suspend fun getById(id: Long): User
    suspend fun getByIdOrNull(id: Long): User?
    suspend fun existsByNickname(nickname: String): Boolean
}