package com.xquare.v1userservice.user.router.dto.saveuser

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 20)
    val accountId: String,

    @field:NotBlank
    val verificationCode: String,

    @field:NotBlank
    val profileFileName: String,

    @field:NotNull
    val password: String
)
