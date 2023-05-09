package com.xquare.v1userservice.user.api.dtos

import java.util.UUID

data class ExcludeUserIdListResponse(
    val userIdList: List<UUID>
)
