package linkpool.link.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.model.Link
import linkpool.link.port.`in`.GetLinksUseCase
import linkpool.link.port.out.LinkPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetLinksService(
    private val linkPort: LinkPort,
  ): GetLinksUseCase {
  override suspend fun getByUserId(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<Link> {
    return linkPort.findPageByUserIdOrderByCreatedDateTimeDesc(userId, paging).let {
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