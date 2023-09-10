package linkpool.link.folder.port.`in`

interface DeleteFolderUseCase {
  suspend fun delete(ownerId: Long, folderId: Long)
}