package linkpool.link.port.`in`

import linkpool.user.model.User

interface CreateLinkUseCase {
  suspend fun create(uid: String, request: SaveLinkRequest)
  suspend fun createBulk(user: User, request: List<SaveLinkRequest>)
}