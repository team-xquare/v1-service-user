package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.annotations.SagaStep
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.saveuser.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.saveuser.api.CreateUserInPendingStateRevert
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import java.util.UUID

@SagaStep
class CreateUserInPendingStateImpl(
    private val userRepositorySpi: UserRepositorySpi
) : CreateUserInPendingStateProcessor, CreateUserInPendingStateRevert {
    override suspend fun processStep(user: User): User {
        return userRepositorySpi.saveUser(user)
    }

    override suspend fun revertStep(userId: UUID) {
        userRepositorySpi.deleteByIdAndStateWithCreatePending(userId)
    }
}
