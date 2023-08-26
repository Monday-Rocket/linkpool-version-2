package linkpool.folder.port.`in`

interface UpdateFolderUseCase {
  suspend fun update(userId: Long, folderId: Long, request: UpdateFolderRequest)
}