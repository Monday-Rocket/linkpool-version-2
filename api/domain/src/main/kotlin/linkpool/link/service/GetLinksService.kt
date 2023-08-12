package linkpool.link.service

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.DomainComponent
import linkpool.link.port.`in`.GetLinksUseCase
import linkpool.link.port.`in`.LinkResponse
import linkpool.link.port.out.LinkPort
import linkpool.user.port.`in`.GetUserUseCase

@DomainComponent
class GetLinksService(
    private val getUserUseCase: GetUserUseCase,
    private val linkPort: LinkPort,
  ): GetLinksUseCase {
  override suspend fun getByUserId(uid: String, paging: LinkPoolPageRequest): LinkPoolPage<LinkResponse> {
    val user = getUserUseCase.getByUid(uid)
    return linkPort.findPageByUserIdOrderByCreatedDateTimeDesc(user.id, paging).let {
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