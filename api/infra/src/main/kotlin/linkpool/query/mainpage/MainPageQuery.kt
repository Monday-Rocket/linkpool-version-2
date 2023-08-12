package linkpool.query.mainpage

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.link.port.`in`.LinkWithUserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MainPageQuery {
  suspend fun getAll(paging: LinkPoolPageRequest, uid: String): LinkPoolPage<LinkWithUserResponse>
  suspend fun getByUserId(userId: Long, paging: LinkPoolPageRequest, uid: String): LinkPoolPage<LinkWithUserResponse>
}