package linkpool.security

import com.google.firebase.auth.FirebaseToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.security.Principal

class LinkPoolAuthentication(
    private val firebaseToken: FirebaseToken?,
    private val id: Long,
    private val uid: String,
): Authentication {


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any? {
        return firebaseToken
    }

    override fun getDetails(): Any? {
        return firebaseToken
    }

    override fun getPrincipal(): Principal {
        return LinkPoolPrincipal(id, uid)
    }


    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        return
    }

   override fun getName(): String {
       return id.toString()
   }
    inner class LinkPoolPrincipal internal constructor(
        val id: Long,
        val uid: String
    ): Principal {
        override fun getName(): String = uid
    }

}