package linkpool.security

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

class SecurityWebFilter(
    private val firebaseClient: FirebaseClient
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val header = exchange.request.headers
            .getFirst("x-auth-token")
            ?: throw UnauthorizedException()

        return firebaseClient.getUserDetailsByToken(header)
            .flatMap { authentication ->
                chain.filter(exchange)
                    .contextWrite { context: Context ->
                        val newContext = ReactiveSecurityContextHolder
                            .withAuthentication(authentication)
                            .readOnly()
                        context.putAll(newContext)
                    }
            }
    }

}
