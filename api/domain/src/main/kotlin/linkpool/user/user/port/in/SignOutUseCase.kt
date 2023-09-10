package linkpool.user2.user.port.`in`

import linkpool.user2.user.model.User

interface SignOutUseCase {
    suspend fun signOut(userId: Long)
}