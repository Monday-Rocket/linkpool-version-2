package linkpool.query.linkfolder.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.common.DomainComponent
import linkpool.query.linkfolder.LinkFolderQuery
import linkpool.query.linkfolder.r2dbc.LinkFolderRepository
import org.springframework.data.domain.Pageable

@DomainComponent
class LinkFolderQueryService(
    private val linkFolderRepository: LinkFolderRepository
): LinkFolderQuery {

  override suspend fun getPageByUserId(userIds: List<Long>, loggedInUserId: Long, pageable: Pageable)
    = linkFolderRepository.findVisiblePageByUserIdIn(userIds, loggedInUserId, pageable).awaitSingle()

}