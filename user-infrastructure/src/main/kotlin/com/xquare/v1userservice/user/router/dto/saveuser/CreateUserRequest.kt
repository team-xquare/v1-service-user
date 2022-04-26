package com.xquare.v1userservice.user.router.dto.saveuser

import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 20)
    val accountId: String,

    @field:NotBlank
    @field:Size(max = 5)
    val name: String,

    @field:NotBlank
    val authCode: String,

    @field:NotNull
    val birthDay: LocalDate,

    @field:PositiveOrZero
    @field:NotNull
    val grade: Int,

    @field:PositiveOrZero
    @field:NotNull
    val classNum: Int,

    @field:PositiveOrZero
    @field:NotNull
    val num: Int,

    @field:NotBlank
    val profileImageUrl: String,

    @field:NotNull
    val password: String
)
