package com.xquare.v1userservice.user.router.dto

import java.util.UUID

data class GetUserGradeAndClassResponse(
    val id: UUID,
    val profileFileName: String?,
    val num: String,
    val name: String,
)
