package com.xquare.v1userservice.user.saveuser.service

data class CreatUserDomainRequest(
    val accountId: String,

    val verificationCode: String,

    val profileFileName: String,

    val password: String
)
