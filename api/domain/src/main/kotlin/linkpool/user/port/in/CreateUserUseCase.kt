package linkpool.user.port.`in`

interface CreateUserUseCase {
    suspend fun createUser(uid: String): CreateUserResponse
}