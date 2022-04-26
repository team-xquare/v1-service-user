package com.xquare.v1userservice.user.handler

import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedEvent
import com.xquare.v1userservice.user.saveuser.api.UpdateUserCreatedStateStepProcessor
import org.springframework.stereotype.Component

@Component
class UserEventHandler(
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor
) {

    suspend fun authorityCreatedEventHandler(authorityCreatedEvent: AuthorityCreatedEvent) {
        updateUserCreatedStateStepProcessor.processStep(authorityCreatedEvent.userId)
    }
}
