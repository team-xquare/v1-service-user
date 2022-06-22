package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Spi

@Spi
interface PasswordEncoderSpi {
    fun encodeString(rawString: String): String
}
