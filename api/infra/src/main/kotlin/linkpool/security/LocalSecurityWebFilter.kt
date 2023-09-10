package linkpool.security

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

class LocalSecurityWebFilter: WebFilter {
    companion object {
        const val LOCAL_USER_ID = 1L
        const val LOCAL_UID = "test-uid"
    }
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange)
            .contextWrite { context: Context ->
                val newContext = ReactiveSecurityContextHolder
                    .withAuthentication(LinkPoolAuthentication(null, LOCAL_USER_ID, LOCAL_UID))
                    .readOnly()
                context.putAll(newContext)
            }
    }
}
