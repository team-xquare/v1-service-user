package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.annotations.SagaStep
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.saveuser.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import java.util.UUID

@SagaStep
class UpdateUserCreatedStateStepImpl(
    private val userRepositorySpi: UserRepositorySpi
) : UpdateUserCreatedStateStepProcessor {
    override suspend fun processStep(userId: UUID) {
        val user = getUserOrThrowUserNotFoundException(userId)
        val changedUser = user.setUserStateToCreated()
        userRepositorySpi.applyChanges(changedUser)
    }

    private suspend fun getUserOrThrowUserNotFoundException(userId: UUID) =
        userRepositorySpi.findByIdAndStateWithCreatePending(userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ON_UPDATE_NOT_FOUND)
}
