package com.xquare.v1userservice.user.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class UserAlreadyExistsException(
    message: String
) : BaseException(message, 409)
