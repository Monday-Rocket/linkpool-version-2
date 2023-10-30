package linkpool.link.link.service

import linkpool.common.DomainComponent
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.`in`.CreateLinkUseCase
import linkpool.link.link.port.`in`.SaveLinkRequest
import linkpool.link.link.port.out.LinkPort
import java.time.LocalDateTime
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class CreateLinkService(
  private val linkPort: LinkPort,
  ): CreateLinkUseCase {
  override suspend fun create(creatorId: Long, request: SaveLinkRequest) {
    linkPort.save(
      Link(
        creatorId = creatorId,
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

  override suspend fun createBulk(creatorId: Long, request: List<SaveLinkRequest>) {
    val links = linkPort.saveAll(request.map {
      Link(
        creatorId = creatorId,
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