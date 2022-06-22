package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest

interface CreateUserApi {
    suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User
}
