package linkpool.common.rest

import linkpool.exception.DomainException
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Component
@Order(-2)
class ExceptionHandler(
    errorAttributes: ErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer,
): AbstractErrorWebExceptionHandler(errorAttributes, webProperties.resources, applicationContext) {
    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        return when (val error = super.getError(request)) {
            is DomainException -> {
                ResponseCode.fromException(error) ?.let { response ->
                    ServerResponse.status(response.httpStatus)
                        .bodyValue(
                            ApiResponse.error(response.statusCode, response.message ?: error.message)
                        )
                } ?: ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue(
                        ApiResponse.error(error.message)
                    )
            }
            else -> {
                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue(
                        ApiResponse.error(error.message)
                    )
            }
        }
    }
}