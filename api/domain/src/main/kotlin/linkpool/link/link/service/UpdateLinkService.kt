package linkpool.link.link.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.link.port.`in`.UpdateLinkRequest
import linkpool.link.link.port.`in`.UpdateLinkUseCase
import linkpool.link.link.port.out.LinkPort
import org.springframework.transaction.annotation.Transactional

@DomainComponent
@Transactional
class UpdateLinkService(
  private val linkPort: LinkPort,
): UpdateLinkUseCase {
  override suspend fun update(creatorId: Long, linkId: Long, request: UpdateLinkRequest) {
    val link = linkPort.findById(linkId) ?: throw DataNotFoundException("링크가 존재하지 않습니다. id: $linkId")
    if (!link.isOwner(creatorId)) throw NotAuthorizedForDataException()

    request.url ?.let { url ->
      link.updateUrl(url)
    }
    request.title ?.let { title ->
      link.updateTitle(title)
    }
    request.describe ?.let { describe ->
      link.updateDescribe(describe)
    }
    request.image ?.let { image ->
      link.updateImage(image)
    }
    linkPort.update(link)
  }
}