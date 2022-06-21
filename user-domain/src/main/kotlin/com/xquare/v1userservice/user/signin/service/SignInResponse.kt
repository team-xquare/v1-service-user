package com.xquare.v1userservice.user.signin.service

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String
)
