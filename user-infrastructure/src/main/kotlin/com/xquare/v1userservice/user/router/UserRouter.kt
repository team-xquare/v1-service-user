package com.xquare.v1userservice.user.router

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
            PATCH("", userHandler::updateUserProfileFileNameHandler)
            POST("", userHandler::saveUserHandler)
            GET("", userHandler::getUserProfileHandler)
            POST("/login", userHandler::userSignInHandler)
            PUT("/login", userHandler::userTokenRefreshHandler)
            GET("/id/{userId}", userHandler::getUserByIdHandler)
            GET("/id", userHandler::getUserByIdsInHandler)
            GET("/account-id/{accountId}", userHandler::getUserByAccountIdHandler)
            GET("/tokens/fcm", userHandler::getUserDeviceTokensHandler)
            GET("/simple", userHandler::getUserPointHandler)
            GET("/class", userHandler::getUserByGradeAndClassHandler)
            GET("/all", userHandler::getAllStudentHandler)
            GET("/teachers", userHandler::getAllTeacherHandler)
            GET("/search", userHandler::getAllStudentByNameHandler)
        }
    }
}
