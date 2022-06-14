package com.xquare.v1userservice.user.signin.spi

interface PasswordMatcherSpi {
    fun rawPasswordAndEncodedPasswordMatches(rawPassword: String, encodedPassword: String): Boolean
}
