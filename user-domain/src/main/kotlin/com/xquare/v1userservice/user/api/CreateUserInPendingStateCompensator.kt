package com.xquare.v1userservice.user.api

import java.util.UUID

interface CreateUserInPendingStateCompensator {
    suspend fun revertStep(userId: UUID)
}
