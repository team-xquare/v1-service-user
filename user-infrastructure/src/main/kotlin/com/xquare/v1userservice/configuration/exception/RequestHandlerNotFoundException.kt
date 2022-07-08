package com.xquare.v1userservice.configuration.exception

class RequestHandlerNotFoundException(
    errorMessage: String
) : BaseError(errorMessage, 404)
