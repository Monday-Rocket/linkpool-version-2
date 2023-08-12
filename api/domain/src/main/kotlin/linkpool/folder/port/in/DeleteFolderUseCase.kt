package linkpool.folder.port.`in`

interface DeleteFolderUseCase {
  suspend fun delete(uid: String, folderId: Long)
}