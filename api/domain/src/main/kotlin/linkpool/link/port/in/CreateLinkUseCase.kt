package linkpool.link.port.`in`

import linkpool.user.model.User

interface CreateLinkUseCase {
  suspend fun create(userId: Long, request: SaveLinkRequest)
  suspend fun createBulk(userId: Long, request: List<SaveLinkRequest>)
}