package com.xquare.v1userservice.user.spi.dtos

import java.util.*

class SaveUserTestAuthorityRequest(
    val userId: UUID,
    val authorityNames: List<String>
)
