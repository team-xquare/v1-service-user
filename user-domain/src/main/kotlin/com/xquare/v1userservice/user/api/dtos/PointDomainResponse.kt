package com.xquare.v1userservice.user.api.dtos

data class PointDomainResponse(
    val userName: String,
    val goodPoint: Int,
    val badPoint: Int
)
