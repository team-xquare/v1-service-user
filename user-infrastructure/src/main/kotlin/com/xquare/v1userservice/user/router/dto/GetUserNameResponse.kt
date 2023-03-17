package com.xquare.v1userservice.user.router.dto

import java.util.UUID

data class GetUserNameResponse(
    val id: UUID,

    val name: String,

    val num: String
)
