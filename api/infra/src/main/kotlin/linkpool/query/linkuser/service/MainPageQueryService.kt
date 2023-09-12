package linkpool.query.linkuser.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.common.persistence.toLinkPoolPage
import linkpool.common.persistence.toPageRequest
import linkpool.query.linkuser.MainPageQuery
import linkpool.query.linkuser.r2dbc.LinkWithUserResult
import linkpool.query.linkuser.r2dbc.MainPageRepository
import linkpool.user.user.port.`in`.GetUserUseCase
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@DomainComponent
@Transactional(readOnly = true)
class MainPageQueryService(
    private val mainPageRepository: MainPageRepository,
    private val getUserUseCase: GetUserUseCase
): MainPageQuery {
    override suspend fun getAll(paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResult> {
        return mainPageRepository.findAll(loggedInUserId, paging.toPageRequest()).toLinkPoolPage()
    }
    override suspend fun getByJobGroupId(jobGroupId: Long, paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResult> {
        return mainPageRepository.findByJobGroup(jobGroupId, loggedInUserId, paging.toPageRequest()).toLinkPoolPage()
    }
    private fun toModel(pages: Mono<Page<LinkWithUserResult>>): Mono<LinkPoolPage<LinkWithUserResult>> =
        pages.map { page ->
            LinkPoolPage(
                page_no = page.number,
                page_size = page.size,
                total_count = page.totalElements,
                total_page = page.totalPages,
                contents = page.content
            )
        }
}