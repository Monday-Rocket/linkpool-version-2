package linkpool.query.linkfolder.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.query.linkfolder.LinkFolderQuery
import linkpool.query.linkfolder.r2dbc.LinkFolderRepository

@DomainComponent
class LinkFolderQueryService(
    private val linkFolderRepository: LinkFolderRepository
): LinkFolderQuery {

  override suspend fun getPageByUserId(userIds: List<Long>, loggedInUserId: Long, paging: LinkPoolPageRequest)
    = linkFolderRepository.findVisiblePageByUserIdIn(userIds, loggedInUserId, paging).awaitSingle()

}