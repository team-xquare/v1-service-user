package com.xquare.v1userservice.user.router.dto.saveuser

import java.time.LocalDate

data class CreateUserRequest(
    val accountId: String,
    val name: String,
    val authCode: String,
    val birthDay: LocalDate,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val profileImageUrl: String,
    val password: String
)
