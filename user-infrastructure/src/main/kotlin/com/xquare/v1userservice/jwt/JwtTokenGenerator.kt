package com.xquare.v1userservice.jwt

interface JwtTokenGenerator {
    fun generateToken(subject: String, params: Map<String, Any>): String
}
