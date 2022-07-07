package com.xquare.v1userservice.user.verificationcode.mapper

import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.VerificationCodeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface VerificationCodeMapper {
    @Mapping(target = "isUsed", ignore = true)
    fun verificationCodeEntityToDomain(verificationCodeEntity: VerificationCodeEntity): VerificationCode
}
