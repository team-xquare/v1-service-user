package com.xquare.v1userservice.user.signin.service

import java.time.LocalDateTime

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
    val expireAt: LocalDateTime
)
