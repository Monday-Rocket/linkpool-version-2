package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.link.link.port.`in`.LinkEventListener
import linkpool.link.link.port.out.LinkPort
import linkpool.user.user.model.UserSignedOutEvent

@DomainComponent
class LinkEventListenerService(
    private val linkPort: LinkPort
) : LinkEventListener {
    override suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent) {
        linkPort.deleteBatchByCreatorId(userSignedOutEvent.userId)
    }
}