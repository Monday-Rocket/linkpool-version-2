package linkpool.query.linkuser.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.common.persistence.toLinkPoolPage
import linkpool.common.persistence.toPageRequest
import linkpool.query.linkuser.SearchLinkQuery
import linkpool.query.linkuser.r2dbc.LinkWithUserResult
import linkpool.query.linkuser.r2dbc.SearchLinkRepository
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional(readOnly = true)
class SearchLinkQueryService(
    private val searchLinkRepository: SearchLinkRepository,
): SearchLinkQuery {
    override suspend fun searchByKeyword(loggedInUserId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult> {
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByTitleContains(loggedInUserId, processedKeyword, paging.toPageRequest()).toLinkPoolPage()
    }

    override suspend fun searchMyLinkByKeyword(creatorId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult> {
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByCreatorIdAndTitleContains(creatorId, processedKeyword, paging.toPageRequest()).toLinkPoolPage()
    }
    private fun preprocessKeyword(keyword: String) = keyword.deleteSpace()

    private fun String.deleteSpace() = this.filterNot { it.isWhitespace() }

}