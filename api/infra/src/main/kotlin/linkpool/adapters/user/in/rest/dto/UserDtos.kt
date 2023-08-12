package linkpool.adapters.user.`in`.rest.dto

data class ApiCreateUserResponse(
    val id: Long,
    val isNew: Boolean
)

data class ApiUpdateUserRequest(
    val nickname: String?,
    val job_group_id: Long?,
    val profile_img: String?
)

class ApiUserInfoRequest(
    val nickname: String?,
    val job_group_id: Long?,
    val profile_img: String?
)


