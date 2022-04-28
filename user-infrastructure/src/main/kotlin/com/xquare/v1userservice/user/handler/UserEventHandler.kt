package com.xquare.v1userservice.user.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xquare.v1userservice.configuration.cdc.DebeziumMessage
import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedEvent
import com.xquare.v1userservice.user.saveuser.api.UpdateUserCreatedStateStepProcessor
import org.springframework.stereotype.Component

@Component
class UserEventHandler(
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor,
    private val objectMapper: ObjectMapper
) {

    suspend fun authorityCreatedEventHandler(debeziumMessage: DebeziumMessage) {
        val payload = debeziumMessage.after.payload
        val authorityCreatedEvent = objectMapper.readValue<AuthorityCreatedEvent>(payload)
        updateUserCreatedStateStepProcessor.processStep(authorityCreatedEvent.userId)
    }
}
