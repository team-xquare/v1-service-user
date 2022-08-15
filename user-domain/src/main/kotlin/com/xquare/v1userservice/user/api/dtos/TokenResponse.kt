package com.xquare.v1userservice.user.api.dtos

import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expireAt: LocalDateTime
)
