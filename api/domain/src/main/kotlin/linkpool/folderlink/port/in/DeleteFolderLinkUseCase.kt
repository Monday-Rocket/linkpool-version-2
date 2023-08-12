package linkpool.folderlink.port.`in`

interface DeleteFolderLinkUseCase {
  suspend fun delete(uid: String, folderId: Long)
}