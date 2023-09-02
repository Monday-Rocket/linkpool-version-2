package linkpool.query.userjobgroup.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.query.userjobgroup.r2dbc.UserWithJobGroupResult
import linkpool.query.userjobgroup.UserJobGroupQuery
import linkpool.query.userjobgroup.r2dbc.UserJobGroupRepository
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional(readOnly = true)
class UserJobGroupQueryService(
    private val userJobGroupRepository: UserJobGroupRepository
): UserJobGroupQuery {
    override suspend fun getInformationById(userId: Long): UserWithJobGroupResult {
        return userJobGroupRepository.findInformationById(userId) ?: throw DataNotFoundException("회원이 존재하지 않습니다. id: $userId")
    }
    override suspend fun getInformationByIdOrNull(userId: Long): UserWithJobGroupResult? {
        return userJobGroupRepository.findInformationById(userId)
    }
}