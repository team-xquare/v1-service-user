package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.jwt.JwtTokenGenerator
import com.xquare.v1userservice.user.TokenType
import com.xquare.v1userservice.user.signin.spi.JwtTokenGeneratorSpi
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class JwtTokenGeneratorSpiImpl(
    private val jwtTokenGenerator: JwtTokenGenerator
) : JwtTokenGeneratorSpi {
    override fun generateJwtToken(subject: String, tokenType: TokenType, params: MutableMap<String, Any>): String {
        params["type"] = tokenType.name
        return jwtTokenGenerator.generateToken(subject, params)
    }

    private fun List<String>.toMultiValueMap() =
        LinkedMultiValueMap<String, String>()
            .apply { put("authorities", this@toMultiValueMap) }
}
