package com.xquare.v1userservice.jwt

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.xquare.v1userservice.jwt.properties.JwtProperties
import com.xquare.v1userservice.user.TokenType
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class JwtToken(
    private val jwtProperties: JwtProperties
) : JwtTokenGenerator {
    override fun generateToken(subject: String, params: Map<String, Any>, tokenType: TokenType): String {
        val expiration = getTokenExpiration(tokenType)
        val signer: JWSSigner = MACSigner(jwtProperties.secretKey)

        val claimsSetBuilder = JWTClaimsSet.Builder()
            .subject(subject)
            .expirationTime(expiration)

        for (param in params) {
            claimsSetBuilder.claim(param.key, param.value)
        }

        val claimsSet = claimsSetBuilder.build()

        val header = JWSHeader.Builder(JWSAlgorithm.HS256)
            .type(JOSEObjectType.JWT)
            .build()

        val signedJWT = SignedJWT(header, claimsSet)
        signedJWT.sign(signer)

        return signedJWT.serialize()
    }

    private fun getTokenExpiration(tokenType: TokenType): Timestamp {
        val expiration = if (tokenType == TokenType.ACCESS_TOKEN) {
            LocalDateTime.now().plusHours(jwtProperties.getAccessTokenExpirationAsHour())
        } else {
            LocalDateTime.now().plusHours(jwtProperties.getRefreshTokenExpirationAsHour())
        }
        return Timestamp.valueOf(expiration)
    }
}
