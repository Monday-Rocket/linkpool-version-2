package linkpool.link.port.`in`

interface DeleteLinkUseCase {
  suspend fun delete(uid: String, linkId: Long)
  suspend fun deleteByFolder(folderId: Long)

}