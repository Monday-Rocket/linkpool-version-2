package linkpool.user.adapters.user.auth

import linkpool.security.FirebaseClient
import linkpool.user.user.port.out.UserAuthPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!local")
@Service
class UserAuthAdapter(
    private val firebaseClient: FirebaseClient
): UserAuthPort {
    override suspend fun setUserId(uid: String, id: Long) {
        return firebaseClient.updateFirebaseUserId(uid, id)
    }
}