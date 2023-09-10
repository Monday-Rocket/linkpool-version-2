package linkpool.user2.user.port.`in`

interface UpdateUserUseCase {
    suspend fun updateProfile(userId: Long, profileRequest: ProfileRequest)
}