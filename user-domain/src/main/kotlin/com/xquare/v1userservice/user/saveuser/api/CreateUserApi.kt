package com.xquare.v1userservice.user.saveuser.api

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.saveuser.service.CreatUserDomainRequest

interface CreateUserApi {
    suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User
}
