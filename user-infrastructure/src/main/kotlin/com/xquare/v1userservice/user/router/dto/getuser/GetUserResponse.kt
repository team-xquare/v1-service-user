package com.xquare.v1userservice.user.router.dto.getuser

import java.time.LocalDate
import java.util.UUID

data class GetUserResponse(
    val id: UUID,

    val accountId: String,

    val name: String,

    val birthDay: LocalDate,

    val grade: Int,

    val classNum: Int,

    val num: Int,

    val profileFileName: String,

    val password: String
)
