package com.xquare.v1userservice.user.api.dtos

import com.xquare.v1userservice.user.UserRole
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expireAt: LocalDateTime,
    val role: UserRole
)
