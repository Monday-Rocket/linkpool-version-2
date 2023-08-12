package linkpool.query.userjobgroup.r2dbc

data class UserWithJobGroupResult(
    val id: Long,
    val nickname: String?,
    val jobGroup: JobGroupResult?,
    val profileImage: String?
)

