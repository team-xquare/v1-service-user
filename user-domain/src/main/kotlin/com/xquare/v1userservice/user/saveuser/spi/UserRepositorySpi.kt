package com.xquare.v1userservice.user.saveuser.spi

import com.xquare.v1userservice.annotations.Spi
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserState
import java.util.UUID

@Spi
interface UserRepositorySpi {
    suspend fun saveUserAndOutbox(user: User): User
    suspend fun findByIdAndStateOrNull(id: UUID, state: UserState): User?
    suspend fun applyChanges(user: User): User
    suspend fun deleteByIdAndState(id: UUID, userState: UserState): User?
}
