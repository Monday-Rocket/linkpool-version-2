package linkpool.folder.port.`in`

import linkpool.user.model.User

interface CreateFolderUseCase {
  suspend fun create(userId: Long, request: SaveFolderRequest)
  suspend fun createBulk(userId: Long, request: List<SaveFolderRequest>)
}