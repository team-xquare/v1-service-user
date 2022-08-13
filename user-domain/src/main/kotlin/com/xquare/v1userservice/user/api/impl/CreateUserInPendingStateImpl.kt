package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.SagaStep
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.CreateUserInPendingStateCompensator
import com.xquare.v1userservice.user.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import java.util.UUID

@SagaStep
class CreateUserInPendingStateImpl(
    private val userRepositorySpi: UserRepositorySpi
) : CreateUserInPendingStateProcessor, CreateUserInPendingStateCompensator {
    override suspend fun processStep(user: User): User {
        return userRepositorySpi.saveUser(user)
    }

    override suspend fun revertStep(userId: UUID) {
        userRepositorySpi.deleteByIdAndStateWithCreatePending(userId)
    }
}
