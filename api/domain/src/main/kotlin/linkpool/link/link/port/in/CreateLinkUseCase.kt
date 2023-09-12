package linkpool.link.link.port.`in`

interface CreateLinkUseCase {
  suspend fun create(creatorId: Long, request: SaveLinkRequest)
  suspend fun createBulk(creatorId: Long, request: List<SaveLinkRequest>)
}