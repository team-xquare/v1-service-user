package com.xquare.v1userservice.user.handler

import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedFailedEvent
import com.xquare.v1userservice.user.saveuser.api.CreateUserInPendingStateRevert
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class UserExceptionEventHandler(
    private val createUserInPendingStateRevert: CreateUserInPendingStateRevert,
    private val updateUserCreatedStateRevert: CreateUserInPendingStateRevert
) {

    suspend fun exceptionHandler(
        authorityCreatedFailedEvent: AuthorityCreatedFailedEvent,
        topic: String
    ) {
        when (topic) {
            "authority" -> revertSinceCreate(authorityCreatedFailedEvent.userId)
        }
    }

    private suspend fun revertSinceCreate(userId: UUID) {
        updateUserCreatedStateRevert.revertStep(userId)
        createUserInPendingStateRevert.revertStep(userId)
    }
}
