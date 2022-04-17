package com.xquare.v1userservice.configuration.exception

class InternalServerError(
    errorMessage: String,
    statusCode: Int = 500
) : BaseError(errorMessage, statusCode) {
    companion object {
        const val UNEXPECTED_EXCEPTION = "Unexpected Error Occurred"
    }
}