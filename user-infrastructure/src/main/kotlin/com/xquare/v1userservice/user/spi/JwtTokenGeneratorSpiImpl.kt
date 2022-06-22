package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.jwt.JwtTokenGenerator
import com.xquare.v1userservice.jwt.properties.JwtProperties
import com.xquare.v1userservice.user.TokenType
import org.springframework.stereotype.Component

@Component
class JwtTokenGeneratorSpiImpl(
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val jwtProperties: JwtProperties
) : JwtTokenGeneratorSpi {
    override fun generateJwtToken(subject: String, tokenType: TokenType, params: MutableMap<String, Any>): String {
        params["type"] = tokenType.name
        return jwtTokenGenerator.generateToken(subject, params)
    }

    override fun getAccessTokenExpirationAsHour(): Int {
        return jwtProperties.accessTokenProperties.expirationAsHour
    }
}
