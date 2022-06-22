package com.xquare.v1userservice.user.spi

interface PasswordMatcherSpi {
    fun passwordMatches(rawPassword: String, encodedPassword: String): Boolean
}
