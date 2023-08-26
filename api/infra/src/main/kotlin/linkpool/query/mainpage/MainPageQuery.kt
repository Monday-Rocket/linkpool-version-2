package linkpool.query.mainpage

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.port.`in`.LinkWithUserResponse

interface MainPageQuery {
  suspend fun getAll(paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResponse>
  suspend fun getByUserId(userId: Long, paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResponse>
}