package linkpool.query.linkfolder

import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LinkFolderQuery {
  suspend fun getPageByUserId(userIds: List<Long>, loggedInUserId: Long, pageable: Pageable): Page<LinkR2dbcEntity> }