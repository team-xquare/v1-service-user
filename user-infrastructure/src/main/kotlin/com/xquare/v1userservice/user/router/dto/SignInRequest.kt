package com.xquare.v1userservice.user.router.dto

import javax.validation.constraints.NotNull

data class SignInRequest(
    @field:NotNull
    val accountId: String,
    @field:NotNull
    val password: String,
    @field:NotNull
    val deviceToken: String
)
