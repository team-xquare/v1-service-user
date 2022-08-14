package com.xquare.v1userservice.user.router.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignInRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 20)
    val accountId: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val deviceToken: String
)
