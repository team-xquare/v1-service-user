package com.xquare.v1userservice.user.signin.service

data class SignInDomainRequest(
    val accountId: String,
    val password: String
)
