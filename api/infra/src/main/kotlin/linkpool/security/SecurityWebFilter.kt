package linkpool.security

import org.springframework.context.annotation.Profile
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

@Profile("prod")
@Component
class SecurityWebFilter(
    private val authService: AuthService
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = exchange.request.headers
            .getFirst("x-auth-token")
            ?: throw UnauthorizationException()

        return authService.getNameByToken(token)
            .map { uid ->
                LinkPoolAuthentication(uid)
            }.flatMap { authentication ->
                chain.filter(exchange)
                    .contextWrite { context: Context ->
                        val newContext = ReactiveSecurityContextHolder
                            .withAuthentication(authentication).readOnly()
                        context.putAll(newContext)
                    }
            }
    }

}
