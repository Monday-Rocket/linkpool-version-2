package linkpool.link.service

import linkpool.common.DomainComponent
import linkpool.link.port.`in`.LinkEventListener
import linkpool.link.port.out.LinkPort
import linkpool.user.model.UserSignedOutEvent

@DomainComponent
class LinkEventListenerService(
    private val linkPort: LinkPort
) : LinkEventListener {
    override suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent) {
        linkPort.deleteBatchByUserId(userSignedOutEvent.userId)
    }
}