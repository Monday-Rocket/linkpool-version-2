package linkpool.user.user.port.`in`

interface CreateUserUseCase {
    suspend fun createUser(uid: String): CreateUserResponse
}