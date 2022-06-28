package com.xquare.v1userservice.user.refreshtoken.exceptions

import com.xquare.v1userservice.configuration.exception.BaseError

class RefreshTokenSaveFailedException(
    errorMessage: String
) : BaseError(errorMessage, 500)
