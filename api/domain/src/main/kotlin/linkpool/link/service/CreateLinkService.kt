package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.link.model.InflowType
import linkpool.link.model.Link
import linkpool.link.port.`in`.CreateLinkUseCase
import linkpool.link.port.`in`.SaveLinkRequest
import linkpool.link.port.out.LinkPort
import linkpool.user.model.User
import linkpool.user.port.`in`.GetUserUseCase
import java.time.LocalDateTime
import javax.transaction.Transactional

@DomainComponent
@Transactional
class CreateLinkService(
  private val getUserUseCase: GetUserUseCase,
  private val linkPort: LinkPort,
  ): CreateLinkUseCase {
  override suspend fun create(uid: String, request: SaveLinkRequest) {
    val user = getUserUseCase.getByUid(uid)
    linkPort.save(
      Link(
        userId = user.id,
        url = request.url,
        title = request.title,
        describe = request.describe,
        image = request.image,
        folderId = request.folderId,
        createdDateTime = request.createdAt ?: LocalDateTime.now(),
        inflowType = request.inflowType ?: InflowType.CREATE
      )
    )
  }

  override suspend fun createBulk(user: User, request: List<SaveLinkRequest>) {
    val links = linkPort.saveAll(request.map {
      Link(
        userId = user.id,
        url = it.url,
        title = it.title,
        describe = it.describe,
        image = it.image,
        folderId = it.folderId,
        createdDateTime = it.createdAt ?: LocalDateTime.now(),
        inflowType = it.inflowType ?: InflowType.CREATE
      )
    })
    val folderIdSet = mutableSetOf<Long>()
    links.forEach {
      it.folderId ?.let  { folderId ->
        folderIdSet.add(folderId)
      }
    }
//        folderIdSet.forEach {
//            applicationEventPublisher.publishEvent(FolderLinkUpdatedEvent(it))
//        }
  }
}