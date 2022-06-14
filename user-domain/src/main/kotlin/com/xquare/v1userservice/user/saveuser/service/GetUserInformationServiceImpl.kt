package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.saveuser.api.GetUserInformationService
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import java.util.UUID

@DomainService
class GetUserInformationServiceImpl(
    private val userRepositorySpi: UserRepositorySpi
) : GetUserInformationService {
    override suspend fun getUserById(userId: UUID): User {
        return userRepositorySpi.findByIdAndStateWithCreated(userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
    }

    override suspend fun getUserByAccountId(accountId: String): User {
        return userRepositorySpi.findByAccountIdAndStateWithCreated(accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
    }
}
