package linkpool.user.port.`in`

import linkpool.jobgroup.model.JobGroup
import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.user.model.User

data class UserResponse(
    val id: Long,
    val nickname: String?,
    val jobGroup: JobGroupResponse?,
    val profileImg: String?
) {
    constructor(user: User, jobGroup: JobGroup) : this(
        user.id,
        user.info?.nickname,
        JobGroupResponse(jobGroup),
        user.info?.profileImage
    )
}

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


