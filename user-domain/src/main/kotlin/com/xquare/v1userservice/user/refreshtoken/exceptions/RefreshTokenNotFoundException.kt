package com.xquare.v1userservice.user.refreshtoken.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class RefreshTokenNotFoundException(
    errorMessage: String
) : BaseException(errorMessage, 404)
