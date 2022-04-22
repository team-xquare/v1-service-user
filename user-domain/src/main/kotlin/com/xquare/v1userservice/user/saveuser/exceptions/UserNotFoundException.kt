package com.xquare.v1userservice.user.saveuser.exceptions

import com.xquare.v1userservice.exceptions.BaseException

class UserNotFoundException(
    message: String
) : BaseException(message, 404) {
    companion object {
        const val USER_ON_DELETE_NOT_FOUND = "User Not Found When Try To Update State"
    }
}
