package com.xquare.v1userservice.user.spi.dtos

import java.util.UUID

class SaveUserTestAuthorityRequest(
    val userId: UUID,
    val authorityNames: List<String>,
)
