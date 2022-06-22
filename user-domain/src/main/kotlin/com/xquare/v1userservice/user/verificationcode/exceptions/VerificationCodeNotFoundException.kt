package com.xquare.v1userservice.user.verificationcode.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class VerificationCodeNotFoundException(
    errorMessage: String
): BaseException(errorMessage, 404)
