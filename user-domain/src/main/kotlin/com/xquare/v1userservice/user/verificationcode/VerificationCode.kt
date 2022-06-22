package com.xquare.v1userservice.user.verificationcode

import java.time.LocalDate
import java.util.UUID

class VerificationCode(
    val id: UUID,

    val code: String,

    val isUsed: Boolean,

    val expiredAt: LocalDate,

    val userName: String,

    val grade: Int,

    val classNum: Int,

    val num: Int,

    val birthDay: LocalDate,

    val entranceYear: Int
)
