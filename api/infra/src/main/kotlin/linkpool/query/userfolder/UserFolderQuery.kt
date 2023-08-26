package linkpool.query.userfolder

import linkpool.query.userfolder.r2dbc.UserFolderListResult

interface UserFolderQuery {
  suspend fun findFoldersByUserId(userId: Long): List<UserFolderListResult>
  suspend fun findFoldersById(id: Long): List<UserFolderListResult>
}