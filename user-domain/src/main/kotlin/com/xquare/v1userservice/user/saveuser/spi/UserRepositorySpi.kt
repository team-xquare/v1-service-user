package com.xquare.v1userservice.user.saveuser.spi

import com.xquare.v1userservice.annotations.Spi
import com.xquare.v1userservice.user.User
import java.util.UUID

@Spi
interface UserRepositorySpi {
    suspend fun saveUserAndOutbox(user: User): User
    suspend fun findByIdAndStateWithCreatePending(id: UUID): User?
    suspend fun applyChanges(user: User): User
    suspend fun deleteByIdAndStateWithCreatePending(id: UUID)
}
