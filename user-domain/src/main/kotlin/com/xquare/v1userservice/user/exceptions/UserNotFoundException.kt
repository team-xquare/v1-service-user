package com.xquare.v1userservice.user.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class UserNotFoundException(
    message: String
) : BaseException(message, 404) {
    companion object {
        const val USER_ON_UPDATE_NOT_FOUND = "User Not Found When Try To Update State"
        const val USER_ID_NOT_FOUND = "User Not Found When Try To Select By Id"
    }
}
