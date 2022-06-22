package com.xquare.v1userservice.user.spi

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PasswordEncoderSpiImpl(
    private val passwordEncoder: PasswordEncoder
) : PasswordMatcherSpi, PasswordEncoderSpi {
    override fun passwordMatches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

    override fun encodeString(rawString: String): String {
        return passwordEncoder.encode(rawString)
    }
}
