package linkpool.link.port.`in`

interface UpdateLinkUseCase {
  suspend fun update(userId: Long, linkId: Long, request: UpdateLinkRequest)

}