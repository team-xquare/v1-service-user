package com.xquare.v1userservice.user.saveuser.api

import com.xquare.v1userservice.user.User

interface UserSignUpApi {
    suspend fun saveUser(user: User): User
}
