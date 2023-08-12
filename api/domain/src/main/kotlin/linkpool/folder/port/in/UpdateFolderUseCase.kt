package linkpool.folder.port.`in`

interface UpdateFolderUseCase {
  suspend fun update(uid: String, folderId: Long, request: UpdateFolderRequest)
}