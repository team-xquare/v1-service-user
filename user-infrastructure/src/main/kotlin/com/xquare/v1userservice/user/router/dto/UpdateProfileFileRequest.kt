package com.xquare.v1userservice.user.router.dto

import javax.validation.constraints.Pattern

data class UpdateProfileFileRequest(
    @field:Pattern(regexp = "^.*\\.(jpg|jpeg|png|heic|webp)\$")
    val profileFileName: String?
)
