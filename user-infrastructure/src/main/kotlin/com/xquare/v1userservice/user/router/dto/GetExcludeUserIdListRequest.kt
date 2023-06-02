package com.xquare.v1userservice.user.router.dto

import java.util.UUID

data class GetExcludeUserIdListRequest(
    val userIdList: List<UUID>
)
