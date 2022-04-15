package com.xquare.v1userservice.user.api.saveuser

import com.xquare.v1userservice.user.User

interface SaveUserApi {
    suspend fun createNewUserAndStartSagaOrchestration(user: User)
}
