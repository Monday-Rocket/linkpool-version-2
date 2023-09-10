package linkpool.user2.user.model


data class Profile(
    val nickname: String,
    val jobGroupId: Long,
    val profileImage: String? = null,
)
