package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Repository
import com.xquare.v1userservice.user.User
import java.util.UUID

@Repository
interface UserRepositorySpi {
    suspend fun save(user: User): User
    suspend fun findByIdOrNull(id: UUID): User?
}
