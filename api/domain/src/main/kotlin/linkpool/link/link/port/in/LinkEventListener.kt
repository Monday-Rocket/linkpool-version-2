package linkpool.link.link.port.`in`

import linkpool.user.user.model.UserSignedOutEvent


interface LinkEventListener {
    suspend fun deleteBatchAll(userSignedOutEvent: UserSignedOutEvent)
}