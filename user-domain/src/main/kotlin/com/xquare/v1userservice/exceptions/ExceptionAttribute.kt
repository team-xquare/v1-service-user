package com.xquare.v1userservice.exceptions

interface ExceptionAttribute {
    val errorMessage: String
    val statusCode: Int
}
