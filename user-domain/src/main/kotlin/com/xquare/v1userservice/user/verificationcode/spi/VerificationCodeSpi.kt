package com.xquare.v1userservice.user.verificationcode.spi

import com.xquare.v1userservice.user.verificationcode.VerificationCode

interface VerificationCodeSpi {
    suspend fun getByCode(code: String): VerificationCode?
}
