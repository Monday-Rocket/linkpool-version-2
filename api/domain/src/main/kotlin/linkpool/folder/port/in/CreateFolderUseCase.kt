package linkpool.folder.port.`in`

import linkpool.user.model.User

interface CreateFolderUseCase {
  suspend fun create(uid: String, request: SaveFolderRequest)
  suspend fun createBulk(user: User, request: List<SaveFolderRequest>)
}