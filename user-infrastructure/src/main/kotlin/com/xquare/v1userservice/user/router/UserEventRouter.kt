package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.configuration.cdc.DebeziumMessage
import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedFailedEvent
import com.xquare.v1userservice.user.handler.UserEventHandler
import com.xquare.v1userservice.user.handler.UserExceptionEventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Controller

@Controller
class UserEventRouter(
    private val userEventHandler: UserEventHandler,
    private val userExceptionEventHandler: UserExceptionEventHandler,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        const val USER_RESPONSE_TOPIC = "xquare-debezium2.authentication.tbl_outbox"
        const val USER_ERROR_TOPIC_PREFIX = "user-error-"
    }

    @KafkaListener(topics = [USER_RESPONSE_TOPIC], groupId = "user-service")
    fun authorityCreatedEventHandler(
        debeziumMessage: DebeziumMessage
    ) = coroutineScope.launch {
        userEventHandler.authorityCreatedEventHandler(debeziumMessage)
    }

    @KafkaListener(topics = ["${USER_ERROR_TOPIC_PREFIX}authority-creation", "${USER_ERROR_TOPIC_PREFIX}application-creation"], groupId = "user-service")
    fun userErrorHandler(
        authorityCreatedFailedEvent: AuthorityCreatedFailedEvent,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String
    ) = coroutineScope.launch {
        userExceptionEventHandler.exceptionHandler(authorityCreatedFailedEvent, topic)
    }
}
