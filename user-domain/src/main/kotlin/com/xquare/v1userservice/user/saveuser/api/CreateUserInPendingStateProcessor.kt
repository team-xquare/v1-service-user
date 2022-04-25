package com.xquare.v1userservice.user.saveuser.api

import com.xquare.v1userservice.user.User

interface CreateUserInPendingStateProcessor {
    suspend fun processStep(user: User)
}
