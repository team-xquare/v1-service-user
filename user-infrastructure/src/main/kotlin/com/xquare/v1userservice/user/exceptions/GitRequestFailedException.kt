package com.xquare.v1userservice.user.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class GitRequestFailedException(
    message: String,
    statusCode: Int
) : BaseException(message, statusCode)
