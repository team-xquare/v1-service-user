package com.xquare.v1userservice.user.router.dto

import java.time.LocalDate

data class GetUserProfileResponse(
    val accountId: String,

    val name: String,

    val birthDay: LocalDate,

    val grade: Int,

    val classNum: Int,

    val num: Int,

    val profileFileName: String?,
)
