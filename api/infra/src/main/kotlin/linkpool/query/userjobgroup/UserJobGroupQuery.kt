package linkpool.query.userjobgroup

import linkpool.query.userjobgroup.r2dbc.UserWithJobGroupResult

interface UserJobGroupQuery {
    suspend fun getProfileById(userId: Long): UserWithJobGroupResult
    suspend fun getProfileByIdOrNull(userId: Long): UserWithJobGroupResult?
}