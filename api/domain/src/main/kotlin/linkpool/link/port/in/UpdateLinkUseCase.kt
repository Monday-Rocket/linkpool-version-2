package linkpool.link.port.`in`

interface UpdateLinkUseCase {
  suspend fun update(uid: String, linkId: Long, request: UpdateLinkRequest)

}