package com.xquare.v1userservice.user.saveuser.spi

import com.xquare.v1userservice.annotations.Spi

@Spi
interface PasswordEncoderSpi {
    fun encodeString(rawString: String): String
}
