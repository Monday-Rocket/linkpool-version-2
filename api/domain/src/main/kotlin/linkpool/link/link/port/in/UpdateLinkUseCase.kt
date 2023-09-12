package linkpool.link.link.port.`in`

interface UpdateLinkUseCase {
  suspend fun update(creatorId: Long, linkId: Long, request: UpdateLinkRequest)

}