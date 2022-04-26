package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.annotations.SagaStep
import com.xquare.v1userservice.user.UserState
import com.xquare.v1userservice.user.saveuser.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.saveuser.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import java.util.UUID

@SagaStep
class UpdateUserCreatedStateStepImpl(
    private val userRepositorySpi: UserRepositorySpi
) : UpdateUserCreatedStateStepProcessor {
    override suspend fun processStep(userId: UUID) {
        val user = getUserOrThrowUserNotFoundException(userId)
        user.setUserStateToCreated()
        userRepositorySpi.applyChanges(user)
    }

    private suspend fun getUserOrThrowUserNotFoundException(userId: UUID) =
        userRepositorySpi.findByIdAndStateOrNull(userId, UserState.CREATE_PENDING)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ON_UPDATE_NOT_FOUND)
}
