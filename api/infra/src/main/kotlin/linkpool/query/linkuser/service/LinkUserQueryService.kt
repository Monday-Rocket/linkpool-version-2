package linkpool.query.linkuser.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.common.persistence.toLinkPoolPage
import linkpool.common.persistence.toPageRequest
import linkpool.query.linkuser.LinkUserQuery
import linkpool.query.linkuser.r2dbc.LinkUserRepository
import linkpool.query.linkuser.r2dbc.LinkWithUserResult

@DomainComponent
class LinkUserQueryService(
    private val linkUserRepository: LinkUserRepository,
): LinkUserQuery {
    override suspend fun getUnclassifiedLinks(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResult> {
        return linkUserRepository.findUnclassifiedLinks(userId, paging.toPageRequest()).toLinkPoolPage()
    }

    override suspend fun getPageOfMyFolder(
        userId: Long,
        folderId: Long,
        paging: LinkPoolPageRequest
    ): LinkPoolPage<LinkWithUserResult> {
        return linkUserRepository.findPageOfMyFolder(userId, folderId, paging.toPageRequest()).toLinkPoolPage()
    }

}