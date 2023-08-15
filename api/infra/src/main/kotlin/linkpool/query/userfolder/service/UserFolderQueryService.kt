package linkpool.query.userfolder.service

import linkpool.common.DomainComponent
import linkpool.query.userfolder.UserFolderQuery
import linkpool.query.userfolder.r2dbc.UserFolderListResult
import linkpool.query.userfolder.r2dbc.UserFolderRepository
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional(readOnly = true)
class UserFolderQueryService(
  private val userFolderRepository: UserFolderRepository
) : UserFolderQuery {

  override suspend fun findFoldersByUid(uid: String): List<UserFolderListResult>
      = userFolderRepository.findFoldersByUserUid(uid)

  override suspend fun findFoldersById(id: Long): List<UserFolderListResult>
      = userFolderRepository.findFoldersByUserId(id)

}