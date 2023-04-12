package com.xquare.v1userservice.user.router.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 20)
    val accountId: String,

    @field:NotBlank
    val verificationCode: String,

    val profileFileName: String?,

    @field:NotNull
    @field:Pattern(
        regexp = "(?=.*[a-z])(?=.*[0-9])(?=.*[!#$%&'()*+,./:;<=>?@＼^_`{|}~])[a-zA-Z0-9!#$%&'()*+,./:;" +
            "<=>?@＼^_`{|}~]{8,30}$",
        message = "password는 소문자, 숫자, 특수문자가 포함되어야 합니다."
    )
    val password: String
)
