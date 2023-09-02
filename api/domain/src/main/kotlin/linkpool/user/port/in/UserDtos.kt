package linkpool.user.port.`in`

import linkpool.jobgroup.model.JobGroup
import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.user.model.User



data class CreateUserResponse(
    val id: Long,
    val isNew: Boolean
)

data class UpdateUserRequest(
    val nickname: String?,
    val jobGroupId: Long?,
    val profileImage: String?
)

data class UserInfoRequest(
    val nickname: String?,
    val jobGroupId: Long?,
    val profileImage: String?
)


