package com.xquare.v1userservice.user.api.dtos

data class SignInDomainRequest(
    val accountId: String,
    val password: String
)
