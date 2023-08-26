package linkpool.security

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder

suspend fun getPrincipal(): LinkPoolAuthentication.LinkPoolPrincipal {
    val context = ReactiveSecurityContextHolder
        .getContext()
        .awaitSingle()
    return context.authentication.principal as LinkPoolAuthentication.LinkPoolPrincipal
}