package linkpool.user.user.model


data class Profile(
    val nickname: String,
    val jobGroupId: Long,
    val profileImage: String? = null,
)
