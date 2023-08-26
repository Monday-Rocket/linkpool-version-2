package linkpool.link.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.port.`in`.GetLinksUseCase
import linkpool.link.port.`in`.LinkResponse
import linkpool.link.port.out.LinkPort
import javax.transaction.Transactional

@DomainComponent
@Transactional
class GetLinksService(
    private val linkPort: LinkPort,
  ): GetLinksUseCase {
  override suspend fun getByUserId(userId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse> {
    return linkPort.findPageByUserIdOrderByCreatedDateTimeDesc(userId, paging).let {
      LinkPoolPage(
          page_no = it.page_no,
          page_size = it.page_size,
          total_count = it.total_count,
          total_page = it.total_page,
          contents = it.contents.map { link ->
            LinkResponse(
                id = link.id,
                url = link.url,
                title = link.title,
                image = link.image,
                folderId = link.folderId,
                describe = link.describe,
                createdDateTime = link.createdDateTime,
            )
          }
      )
    }
  }
  override suspend fun getByFolderId(folderId: Long, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse> {
    return linkPort.findPageByFolderIdOrderByCreatedDateTimeDesc(folderId, paging).let {
      LinkPoolPage(
          page_no = it.page_no,
          page_size = it.page_size,
          total_count = it.total_count,
          total_page = it.total_page,
          contents = it.contents.map { link ->
            LinkResponse(
                id = link.id,
                url = link.url,
                title = link.title,
                image = link.image,
                folderId = link.folderId,
                describe = link.describe,
                createdDateTime = link.createdDateTime,
            )
          }
      )
    }
  }
}