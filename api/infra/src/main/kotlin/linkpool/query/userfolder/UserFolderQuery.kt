package linkpool.query.userfolder

import linkpool.query.userfolder.r2dbc.UserFolderListResult

interface UserFolderQuery {
  suspend fun findFoldersByUid(uid: String): List<UserFolderListResult>
  suspend fun findFoldersById(id: Long): List<UserFolderListResult>
}