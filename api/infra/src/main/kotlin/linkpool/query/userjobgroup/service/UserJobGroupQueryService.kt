package linkpool.query.userjobgroup.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.query.userjobgroup.r2dbc.UserWithJobGroupResult
import linkpool.query.userjobgroup.UserJobGroupQuery
import linkpool.query.userjobgroup.r2dbc.UserJobGroupRepository

@DomainComponent
class UserJobGroupQueryService(
    private val userJobGroupRepository: UserJobGroupRepository
): UserJobGroupQuery {
    override suspend fun getInformationByUid(uid: String): UserWithJobGroupResult {
        return userJobGroupRepository.findInformationByUid(uid) ?: throw DataNotFoundException("회원이 존재하지 않습니다. uid: $uid")
    }
    override suspend fun getInformationById(userId: Long): UserWithJobGroupResult {
        return userJobGroupRepository.findInformationById(userId) ?: throw DataNotFoundException("회원이 존재하지 않습니다. id: $userId")
    }
    override suspend fun getInformationByUidOrNull(uid: String): UserWithJobGroupResult? {
        return userJobGroupRepository.findInformationByUid(uid)
    }
    override suspend fun getInformationByIdOrNull(userId: Long): UserWithJobGroupResult? {
        return userJobGroupRepository.findInformationById(userId)
    }
}