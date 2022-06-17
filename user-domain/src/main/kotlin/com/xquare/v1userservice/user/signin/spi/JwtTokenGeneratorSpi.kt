package com.xquare.v1userservice.user.signin.spi

import com.xquare.v1userservice.user.TokenType

interface JwtTokenGeneratorSpi {
    fun generateJwtToken(subject: String, tokenType: TokenType, params: MutableMap<String, Any>): String
}
