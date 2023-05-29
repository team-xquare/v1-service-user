package com.xquare.v1userservice.jwt

import com.xquare.v1userservice.user.TokenType

interface JwtTokenGenerator {
    fun generateToken(subject: String, params: Map<String, Any>, tokenType: TokenType): String
}
