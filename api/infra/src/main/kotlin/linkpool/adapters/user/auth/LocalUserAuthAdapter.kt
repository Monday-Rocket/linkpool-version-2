package linkpool.adapters.user.auth

import linkpool.user.port.out.UserAuthPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("local")
@Service
class LocalUserAuthAdapter: UserAuthPort {
    override suspend fun setUserId(uid: String, id: Long) {
        return
    }
}