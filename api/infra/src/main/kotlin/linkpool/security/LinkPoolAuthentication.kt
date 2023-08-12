package linkpool.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.security.Principal

class LinkPoolAuthentication(
    private val name: String
): Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return null
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Principal {
        return Principal {
            return@Principal name
        }
    }


    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        return
    }

   override fun getName(): String {
        return name
    }
}