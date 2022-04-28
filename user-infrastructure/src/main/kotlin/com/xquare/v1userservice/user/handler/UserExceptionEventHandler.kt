package com.xquare.v1userservice.user.handler

import com.xquare.v1userservice.user.event.usercreate.AuthorityCreatedFailedEvent
import com.xquare.v1userservice.user.router.UserEventRouter
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
        when (topic.removePrefixToGetPureErrorType()) {
            "authority-creation" -> revertUserCreation(authorityCreatedFailedEvent.userId)
        }
    }

    private fun String.removePrefixToGetPureErrorType() =
        this.replace(UserEventRouter.USER_ERROR_TOPIC_PREFIX, "")

    private suspend fun revertUserCreation(userId: UUID) {
        createUserInPendingStateRevert.revertStep(userId)
    }
}
