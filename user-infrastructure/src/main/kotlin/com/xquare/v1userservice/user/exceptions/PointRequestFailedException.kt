package com.xquare.v1userservice.user.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class PointRequestFailedException(
    message: String,
    statusCode: Int
) : BaseException(message, statusCode)
