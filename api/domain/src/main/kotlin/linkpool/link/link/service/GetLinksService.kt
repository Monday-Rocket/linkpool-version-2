package linkpool.link.link.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.link.model.Link
import linkpool.link.link.port.`in`.GetLinksUseCase
import linkpool.link.link.port.out.LinkPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetLinksService(
    private val linkPort: LinkPort,
  ): GetLinksUseCase {
  override suspend fun getByCreatorId(creatorId: Long, paging: LinkPoolPageRequest): LinkPoolPage<Link> {
    return linkPort.findPageByCreatorIdOrderByCreatedDateTimeDesc(creatorId, paging).let {
      LinkPoolPage(
          page_no = it.page_no,
          page_size = it.page_size,
          total_count = it.total_count,
          total_page = it.total_page,
          contents = it.contents
        )
    }
  }
}