package linkpool.query.searchlink.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.port.`in`.LinkWithUserResponse
import linkpool.query.searchlink.SearchLinkQuery
import linkpool.query.searchlink.r2dbc.SearchLinkRepository
import linkpool.user.port.`in`.GetUserUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono

@DomainComponent
class SearchLinkQueryService(
    private val searchLinkRepository: SearchLinkRepository,
    private val getUserUseCase: GetUserUseCase
): SearchLinkQuery {
    override suspend fun searchByKeyword(uid: String, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse> {
        val userId = getUserUseCase.getByUid(uid)
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByTitleContains(userId.id, processedKeyword, paging.page_no, paging.page_size).let{
            toModel(it).awaitSingle()
        }
    }

    override suspend fun searchMyLinkByKeyword(uid: String, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse> {
        val userId = getUserUseCase.getByUid(uid)
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByUserIdAndTitleContains(userId.id, processedKeyword, paging.page_no, paging.page_size).let{
            toModel(it).awaitSingle()
        }

    }
    private fun preprocessKeyword(keyword: String) = keyword.deleteSpace()

    private fun String.deleteSpace() = this.filterNot { it.isWhitespace() }

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