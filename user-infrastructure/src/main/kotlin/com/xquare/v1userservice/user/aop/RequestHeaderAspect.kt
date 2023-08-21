package com.xquare.v1userservice.user.aop

import com.xquare.v1userservice.jwt.UnAuthorizedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.UUID

@Component
class RequestHeaderAspect(
    @Value("\${security.value}")
    private val secret: String,
) {

    fun getUserId(serverRequest: ServerRequest): UUID {
        val userId = serverRequest.headers().firstHeader("Request-User-Id")
            ?: throw UnAuthorizedException("UnAuthorized")

        return UUID.fromString(userId)
    }
}
