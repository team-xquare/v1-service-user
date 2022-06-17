package com.xquare.v1userservice.user.spi

import java.util.UUID

data class AuthorityListResponse(
    val authorities: List<AuthorityResponse>
)

data class AuthorityResponse(
    val authorityId: UUID,
    val authorityName: String,
    val description: String
)
