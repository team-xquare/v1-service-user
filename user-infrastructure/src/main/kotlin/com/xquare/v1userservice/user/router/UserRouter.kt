package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.user.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouter {

    @Bean
    fun userBaseRouter(userHandler: UserHandler) = coRouter {
        "/users".nest {
            contentType(MediaType.APPLICATION_JSON)
            POST("", userHandler::saveUserHandler)
            GET("/id/{userId}", userHandler::getUserByIdHandler)
            GET("/account-id/{accountId}", userHandler::getUserByAccountIdHandler)
        }
    }
}
