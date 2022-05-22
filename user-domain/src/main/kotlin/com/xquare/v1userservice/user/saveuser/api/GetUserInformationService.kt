package com.xquare.v1userservice.user.saveuser.api

import com.xquare.v1userservice.user.User
import java.util.UUID

interface GetUserInformationService {
    suspend fun getUserById(userId: UUID): User
    suspend fun getUserByAccountId(accountId: String): User
}
