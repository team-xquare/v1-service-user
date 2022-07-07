package com.xquare.v1userservice.jwt

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.xquare.v1userservice.jwt.properties.JwtProperties
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class JwtToken(
    private val jwtProperties: JwtProperties
) : JwtTokenGenerator {
    override fun generateToken(subject: String, params: Map<String, Any>): String {
        val expiration = getAccessTokenExpiration()
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

    private fun getAccessTokenExpiration(): Timestamp {
        val expiration = LocalDateTime.now().plusHours(jwtProperties.getAccessTokenExpirationAsHour())
        return Timestamp.valueOf(expiration)
    }
}
