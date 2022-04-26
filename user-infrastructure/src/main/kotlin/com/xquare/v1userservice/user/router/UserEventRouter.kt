package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedEvent
import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedFailedEvent
import com.xquare.v1userservice.user.handler.UserEventHandler
import com.xquare.v1userservice.user.handler.UserExceptionEventHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Controller

@Controller
class UserEventRouter(
    private val userEventHandler: UserEventHandler,
    private val userExceptionEventHandler: UserExceptionEventHandler
) {

    companion object {
        const val USER_RESPONSE_TOPIC = "authority-create-response"
        const val USER_ERROR_TOPIC_PREFIX = "user-response"
    }

    @KafkaListener(topics = [USER_RESPONSE_TOPIC], groupId = "user-service")
    suspend fun authorityCreatedEventHandler(
        authorityCreatedEvent: AuthorityCreatedEvent
    ) {
        userEventHandler.authorityCreatedEventHandler(authorityCreatedEvent)
    }

    @KafkaListener(topicPattern = "$USER_ERROR_TOPIC_PREFIX-*", groupId = "user-service")
    suspend fun userErrorHandler(
        authorityCreatedFailedEvent: AuthorityCreatedFailedEvent,
        @Header(KafkaHeaders.TOPIC) topic: String
    ) {
        userExceptionEventHandler.exceptionHandler(authorityCreatedFailedEvent, topic)
    }
}
