package com.xquare.v1userservice.user.router.dto

import java.util.UUID

data class GetTeacherInfoResponse(
    val id: UUID,
    val name: String,
)
