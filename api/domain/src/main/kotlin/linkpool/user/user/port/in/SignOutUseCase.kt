package linkpool.user.user.port.`in`

interface SignOutUseCase {
    suspend fun signOut(userId: Long)
}