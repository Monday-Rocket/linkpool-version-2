package linkpool.link.folder.port.`in`

interface CreateFolderUseCase {
  suspend fun create(ownerId: Long, request: SaveFolderRequest)
  suspend fun createBulk(ownerId: Long, request: List<SaveFolderRequest>)
}