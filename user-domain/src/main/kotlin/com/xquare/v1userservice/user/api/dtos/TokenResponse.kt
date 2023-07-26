package com.xquare.v1userservice.user.api.dtos

import com.xquare.v1userservice.user.UserRole
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val accessTokenExpireAt: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpireAt: LocalDateTime,
    val role: UserRole
)
