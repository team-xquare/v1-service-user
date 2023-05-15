package com.xquare.v1userservice.user.router.dto

import reactor.util.annotation.Nullable
import java.util.UUID

data class GetExcludeUserIdListRequest(
    @field:Nullable
    val userIds: List<UUID>?,
)