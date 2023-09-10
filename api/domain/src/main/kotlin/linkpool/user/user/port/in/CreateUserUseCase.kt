package linkpool.user2.user.port.`in`

interface CreateUserUseCase {
    suspend fun createUser(uid: String): CreateUserResponse
}