package linkpool.link.port.`in`

import linkpool.user.model.UserSignedOutEvent

interface LinkEventListener {
    suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent)
}