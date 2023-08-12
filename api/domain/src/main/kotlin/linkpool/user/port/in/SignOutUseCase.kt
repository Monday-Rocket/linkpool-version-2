package linkpool.user.port.`in`

import linkpool.user.model.User

interface SignOutUseCase {
    suspend fun signOut(user: User)
}