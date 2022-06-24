package com.xquare.v1userservice.user.refreshtoken.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class RefreshTokenSaveFailedException(
    errorMessage: String
): BaseException(errorMessage, 500)
