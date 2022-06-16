package com.xquare.v1userservice.user.signin.spi

interface PasswordMatcherSpi {
    fun passwordMatches(rawPassword: String, encodedPassword: String): Boolean
}
