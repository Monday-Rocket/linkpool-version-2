package linkpool.query.mainpage.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.port.`in`.LinkWithUserResponse
import linkpool.query.mainpage.MainPageQuery
import linkpool.query.mainpage.r2dbc.MainPageRepository
import linkpool.user.port.`in`.GetUserUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@DomainComponent
@Transactional(readOnly = true)
class MainPageQueryService(
    private val mainPageRepository: MainPageRepository,
    private val getUserUseCase: GetUserUseCase
): MainPageQuery {
    override suspend fun getAll(paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResponse> {
        return mainPageRepository.findAll(loggedInUserId, paging).let{
            toModel(it).awaitSingle()
        }
    }
    override suspend fun getByJobGroupId(jobGroupId: Long, paging: LinkPoolPageRequest, loggedInUserId: Long): LinkPoolPage<LinkWithUserResponse> {
        return mainPageRepository.findByJobGroup(jobGroupId, loggedInUserId, paging).let{
            toModel(it).awaitSingle()
        }
    }
    private fun toModel(pages: Mono<Page<LinkWithUserResponse>>): Mono<LinkPoolPage<LinkWithUserResponse>> =
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