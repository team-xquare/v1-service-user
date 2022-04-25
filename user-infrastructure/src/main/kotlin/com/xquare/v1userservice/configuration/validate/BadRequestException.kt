package com.xquare.v1userservice.configuration.validate

import com.xquare.v1userservice.exceptions.BaseException

class BadRequestException(
    errorMessage: String
) : BaseException(errorMessage, 400)
