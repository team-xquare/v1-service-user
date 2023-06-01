package com.xquare.v1userservice.user.api.dtos

import java.util.UUID

data class ExcludeUserIdListRequest(
    val userIdList: List<UUID>
)
