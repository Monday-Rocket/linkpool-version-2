package linkpool.folder.port.`in`

interface DeleteFolderUseCase {
  suspend fun delete(userId: Long, folderId: Long)
}