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
import reactor.core.publisher.Mono

@DomainComponent
class MainPageQueryService(
    private val mainPageRepository: MainPageRepository,
    private val getUserUseCase: GetUserUseCase
): MainPageQuery {
    override suspend fun getAll(paging: LinkPoolPageRequest, uid: String): LinkPoolPage<LinkWithUserResponse> {
        val me = getUserUseCase.getByUid(uid)
        return mainPageRepository.findAll(me.id, PageRequest.of(paging.page_no, paging.page_size)).let{
            toModel(it).awaitSingle()
        }
    }
    override suspend fun getByUserId(jobGroupId: Long, paging: LinkPoolPageRequest, uid: String): LinkPoolPage<LinkWithUserResponse> {
        val me = getUserUseCase.getByUid(uid)
        return mainPageRepository.findByUserId(jobGroupId, me.id, PageRequest.of(paging.page_no, paging.page_size)).let{
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