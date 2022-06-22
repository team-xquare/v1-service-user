package com.xquare.v1userservice.user.api

import java.util.UUID

interface CreateUserInPendingStateRevert {
    suspend fun revertStep(userId: UUID)
}
