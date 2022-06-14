package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.signin.spi.PasswordMatcherSpi
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PasswordMatcherSpiImpl(
    private val passwordEncoder: PasswordEncoder
) : PasswordMatcherSpi {
    override fun rawPasswordAndEncodedPasswordMatches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
