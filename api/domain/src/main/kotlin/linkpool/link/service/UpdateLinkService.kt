package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.exception.NotAuthorizedForDataException
import linkpool.link.port.`in`.UpdateLinkRequest
import linkpool.link.port.`in`.UpdateLinkUseCase
import linkpool.link.port.out.LinkPort
import linkpool.link.port.out.getById
import javax.transaction.Transactional

@DomainComponent
@Transactional
class UpdateLinkService(
  private val linkPort: LinkPort,
): UpdateLinkUseCase {
  override suspend fun update(userId: Long, linkId: Long, request: UpdateLinkRequest) {
    val link = linkPort.getById(linkId)
    if (!link.isSameUser(userId)) throw NotAuthorizedForDataException()

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