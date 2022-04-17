package com.xquare.v1userservice.configuration.exception.handler

data class ErrorResponse(
    val responseStatus: Int,
    val errorMessage: String
)
