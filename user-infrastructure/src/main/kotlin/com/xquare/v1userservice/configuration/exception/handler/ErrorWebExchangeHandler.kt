package com.xquare.v1userservice.configuration.exception.handler

import com.xquare.v1userservice.configuration.exception.BaseError
import com.xquare.v1userservice.configuration.exception.InternalServerError
import com.xquare.v1userservice.exceptions.BaseException
import com.xquare.v1userservice.exceptions.ExceptionAttribute
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Order(-2)
@Component
class ErrorWebExchangeHandler(
    errorAttributes: ErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
    errorAttributes,
    webProperties.resources,
    applicationContext
) {

    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> =
        RouterFunctions.route(RequestPredicates.all(), this::handleError)

    private fun handleError(request: ServerRequest): Mono<ServerResponse> =
        when (val throwable = super.getError(request)) {
            is BaseException -> buildErrorResponse(throwable)
            is BaseError -> buildErrorResponse(throwable)
            else -> buildErrorResponse(InternalServerError(InternalServerError.UNEXPECTED_EXCEPTION))
        }

    private fun buildErrorResponse(exceptionAttribute: ExceptionAttribute) =
        ServerResponse.status(exceptionAttribute.statusCode)
            .bodyValue(
                ErrorResponse(
                    errorMessage = exceptionAttribute.errorMessage,
                    responseStatus = exceptionAttribute.statusCode
                )
            )
}
