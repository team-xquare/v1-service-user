package com.xquare.v1userservice.user.repository

import com.xquare.v1userservice.user.User
import java.util.*

interface UserRepository {
    suspend fun save(user: User): User
    suspend fun findByIdOrNull(id: UUID): User?
}