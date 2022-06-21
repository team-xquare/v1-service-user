package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.saveuser.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.saveuser.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.saveuser.api.UserSignUpApi
import com.xquare.v1userservice.user.saveuser.spi.SaveUserBaseAuthoritySpi

@DomainService
class UserSignUpApiImpl(
    private val createUserInPendingStateProcessor: CreateUserInPendingStateProcessor,
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor,
    private val saveUserBaseAuthoritySpi: SaveUserBaseAuthoritySpi
): UserSignUpApi {
    override suspend fun saveUser(user: User): User {
        val savedUser = createUserInPendingStateProcessor.processStep(user)
        saveUserBaseAuthoritySpi.saveBaseUserAuthority(savedUser.id)
        updateUserCreatedStateStepProcessor.processStep(user.id)
        return savedUser
    }
}