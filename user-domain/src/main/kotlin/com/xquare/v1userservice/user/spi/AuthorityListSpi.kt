package com.xquare.v1userservice.user.spi

import java.util.UUID

interface AuthorityListSpi {
    suspend fun getAuthorities(userId: UUID): List<String>
}
