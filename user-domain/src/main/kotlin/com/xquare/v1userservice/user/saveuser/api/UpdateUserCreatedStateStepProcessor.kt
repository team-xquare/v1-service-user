package com.xquare.v1userservice.user.saveuser.api

import java.util.UUID

interface UpdateUserCreatedStateStepProcessor {
    suspend fun processStep(userId: UUID)
}
