package com.xquare.v1userservice.user.api.dtos

data class CreatUserDomainRequest(
    val accountId: String,

    val verificationCode: String,

    val profileFileName: String?,

    val password: String
)
