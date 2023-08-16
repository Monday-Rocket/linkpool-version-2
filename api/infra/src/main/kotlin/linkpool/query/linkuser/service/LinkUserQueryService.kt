package linkpool.query.linkuser.service

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.common.DomainComponent
import linkpool.query.linkuser.LinkUserQuery
import linkpool.query.linkuser.r2dbc.LinkResponse
import linkpool.query.linkuser.r2dbc.LinkUserRepository
import linkpool.user.port.`in`.GetUserUseCase
import org.springframework.data.domain.Page
import reactor.core.publisher.Mono

@DomainComponent
class LinkUserQueryService(
    private val linkUserRepository: LinkUserRepository,
    private val getUserUseCase: GetUserUseCase
): LinkUserQuery {
    override suspend fun getUnclassifiedLinks(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse> {
        val user = getUserUseCase.getByUid(uid)
        return toModel(linkUserRepository.findUnclassifiedLinks(user.id, paging)).awaitSingle()
    }

    private fun toModel(pages: Mono<Page<LinkR2dbcEntity>>): Mono<LinkPoolPage<LinkResponse>> =
        pages.map { page ->
            LinkPoolPage(
                page_no = page.number,
                page_size = page.size,
                total_count = page.totalElements,
                total_page = page.totalPages,
                contents = page.content.map {entity ->
                    LinkResponse(
                        id = entity.id,
                        url = entity.url,
                        title = entity.title,
                        image = entity.image,
                        folderId = entity.folderId,
                        describe = entity.describe,
                        createdDateTime = entity.createdDateTime,
                    )
                }
            )
        }
}