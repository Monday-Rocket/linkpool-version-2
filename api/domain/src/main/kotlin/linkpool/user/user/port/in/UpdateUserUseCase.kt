package linkpool.user.user.port.`in`

interface UpdateUserUseCase {
    suspend fun updateProfile(userId: Long, profileRequest: ProfileRequest)
}