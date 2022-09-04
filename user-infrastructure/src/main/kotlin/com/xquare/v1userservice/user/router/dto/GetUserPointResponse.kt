package com.xquare.v1userservice.user.router.dto

data class GetUserPointResponse(
    val name: String,
    val goodPoint: Int,
    val badPoint: Int
)
