package linkpool.folderlink.port.`in`

interface DeleteFolderLinkUseCase {
  suspend fun delete(userId: Long, folderId: Long)
}