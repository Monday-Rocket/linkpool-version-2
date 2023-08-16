package linkpool.query.linkfolder

import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.domain.Page

interface LinkFolderQuery {
  suspend fun getPageByUserId(userIds: List<Long>, loggedInUserId: Long, paging: LinkPoolPageRequest): Page<LinkR2dbcEntity> }