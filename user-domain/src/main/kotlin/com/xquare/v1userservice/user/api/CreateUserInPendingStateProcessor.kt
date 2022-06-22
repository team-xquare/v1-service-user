package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.User

interface CreateUserInPendingStateProcessor {
    suspend fun processStep(user: User): User
}
