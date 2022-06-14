package com.xquare.v1userservice.user.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class PasswordNotMatchesException(
    message: String
) : BaseException(message, 404) {
    companion object {
        const val LOGIN_PASSWORD_NOT_MATCHES = "Password Not Matches Exception. Please check password"
    }
}
