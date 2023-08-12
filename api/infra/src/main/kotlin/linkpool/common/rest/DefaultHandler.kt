package linkpool.common.rest

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class DefaultHandler {
    suspend fun getDefault(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
    }
}
