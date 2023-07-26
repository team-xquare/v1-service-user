package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.TokenType

interface JwtTokenGeneratorSpi {
    fun generateJwtToken(subject: String, tokenType: TokenType, params: MutableMap<String, Any>): String
    fun getAccessTokenExpirationAsHour(): Int
    fun getRefreshTokenExpirationAsHour(): Int
}
