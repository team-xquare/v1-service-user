package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import java.util.UUID

interface GetUserDeviceTokensApi {
    suspend fun getUserDeviceTokensByIdIn(ids: List<UUID>): UserDeviceTokenResponse
}
