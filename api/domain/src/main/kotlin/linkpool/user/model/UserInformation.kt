package linkpool.user.model


data class UserInformation(
    val nickname: String,
    val jobGroupId: Long,
    val profileImage: String? = null,
)
