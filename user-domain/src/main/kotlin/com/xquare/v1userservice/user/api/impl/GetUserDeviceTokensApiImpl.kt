package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.api.GetUserDeviceTokensApi
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import java.util.UUID

@DomainService
class GetUserDeviceTokensApiImpl(
    private val userRepositorySpi: UserRepositorySpi
) : GetUserDeviceTokensApi {
    override suspend fun getUserDeviceTokensByIdIn(ids: List<UUID>): UserDeviceTokenResponse {
        val userDeviceTokens = userRepositorySpi.findAllByIdsIn(ids)
            .filter { it.deviceToken != null }
            .map { it.deviceToken!! }

        return UserDeviceTokenResponse(userDeviceTokens)
    }
}
