package com.xquare.v1userservice.user.router.dto.signin

import javax.validation.constraints.NotNull

data class SignInRequest(
    @field:NotNull
    val accountId: String,
    @field:NotNull
    val password: String
)
