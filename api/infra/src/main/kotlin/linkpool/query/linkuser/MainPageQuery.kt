package linkpool.query.linkuser

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.query.linkuser.r2dbc.LinkWithUserResult

interface MainPageQuery {
  suspend fun getAll(paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResult>
  suspend fun getByJobGroupId(userId: Long, paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResult>
}