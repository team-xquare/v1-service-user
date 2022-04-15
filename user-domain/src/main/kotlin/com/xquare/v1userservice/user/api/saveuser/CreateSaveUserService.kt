package com.xquare.v1userservice.user.api.saveuser

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.spi.UserRepositorySpi

@DomainService
class CreateSaveUserService(
    private val userRepositorySpi: UserRepositorySpi
) : SaveUserApi {
    override suspend fun createNewUserAndStartSagaOrchestration(user: User) {
        userRepositorySpi.save(user)
    }
}
