package com.xquare.v1userservice.user.router.dto

import java.util.UUID
import javax.validation.constraints.NotNull

data class GetUserInfoRequest(
    @field: NotNull
    val userIds: List<UUID>,
)
