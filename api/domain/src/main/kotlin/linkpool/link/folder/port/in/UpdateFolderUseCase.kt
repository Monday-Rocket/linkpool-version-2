package linkpool.link.folder.port.`in`

interface UpdateFolderUseCase {
  suspend fun update(ownerId: Long, folderId: Long, request: UpdateFolderRequest)
}