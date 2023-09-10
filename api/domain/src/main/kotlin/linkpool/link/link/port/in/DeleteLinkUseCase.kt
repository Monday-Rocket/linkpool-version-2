package linkpool.link.link.port.`in`

interface DeleteLinkUseCase {
  suspend fun delete(creatorId: Long, linkId: Long)
  suspend fun deleteByFolder(folderId: Long)

}