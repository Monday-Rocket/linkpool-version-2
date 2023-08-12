package linkpool.user.port.`in`

import linkpool.user.model.User

interface UpdateUserUseCase {
    suspend fun updateUserInfo(user: User, userInfoRequest: UserInfoRequest)
}