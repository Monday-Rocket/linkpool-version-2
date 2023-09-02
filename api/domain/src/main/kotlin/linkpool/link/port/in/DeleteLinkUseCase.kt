package linkpool.link.port.`in`

interface DeleteLinkUseCase {
  suspend fun delete(userId: Long, linkId: Long)
  suspend fun deleteByFolder(folderId: Long)

}