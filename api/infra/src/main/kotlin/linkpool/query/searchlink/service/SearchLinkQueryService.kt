package linkpool.query.searchlink.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.port.`in`.LinkWithUserResponse
import linkpool.query.searchlink.SearchLinkQuery
import linkpool.query.searchlink.r2dbc.SearchLinkRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@DomainComponent
@Transactional(readOnly = true)
class SearchLinkQueryService(
    private val searchLinkRepository: SearchLinkRepository,
): SearchLinkQuery {
    override suspend fun searchByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse> {
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByTitleContains(userId, processedKeyword, PageRequest.of(paging.page_no, paging.page_size)).let{
            toModel(it).awaitSingle()
        }
    }

    override suspend fun searchMyLinkByKeyword(userId: Long, keyword: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkWithUserResponse> {
        val processedKeyword = preprocessKeyword(keyword)

        return searchLinkRepository.findPageByUserIdAndTitleContains(userId, processedKeyword, PageRequest.of(paging.page_no, paging.page_size)).let{
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