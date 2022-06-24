package com.xquare.v1userservice.user.refreshtoken

import java.util.UUID

data class RefreshTokenEntity(
    val tokenValue: String,
    val userId: UUID
)
