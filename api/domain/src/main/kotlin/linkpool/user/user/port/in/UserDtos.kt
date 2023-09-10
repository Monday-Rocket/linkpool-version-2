package linkpool.user.user.port.`in`


data class CreateUserResponse(
    val id: Long,
    val isNew: Boolean
)

data class ProfileRequest(
    val nickname: String?,
    val jobGroupId: Long?,
    val profileImage: String?
)


