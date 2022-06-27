package com.xquare.v1userservice.user.api.dtos

import java.time.LocalDateTime

data class TokenRefreshResponse(
    val accessToken: String,
    val expireAt: LocalDateTime
)
