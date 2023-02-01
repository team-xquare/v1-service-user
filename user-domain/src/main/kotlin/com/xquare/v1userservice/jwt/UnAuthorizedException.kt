package com.xquare.v1userservice.jwt

import com.xquare.v1userservice.exceptions.BaseException

class UnAuthorizedException(
    message: String
) : BaseException(message, 401)
