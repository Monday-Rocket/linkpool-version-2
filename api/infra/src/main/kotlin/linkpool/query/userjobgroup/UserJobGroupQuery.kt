package linkpool.query.userjobgroup

import linkpool.query.userjobgroup.r2dbc.UserWithJobGroupResult

interface UserJobGroupQuery {
    suspend fun getInformationByUid(uid: String): UserWithJobGroupResult
    suspend fun getInformationById(userId: Long): UserWithJobGroupResult
    suspend fun getInformationByUidOrNull(uid: String): UserWithJobGroupResult?
    suspend fun getInformationByIdOrNull(userId: Long): UserWithJobGroupResult?
}