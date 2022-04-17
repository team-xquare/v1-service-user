package com.xquare.v1userservice.configuration.exception

import com.xquare.v1userservice.exceptions.ExceptionAttribute

abstract class BaseError(
    override val errorMessage: String,
    override val statusCode: Int
) : RuntimeException(errorMessage), ExceptionAttribute