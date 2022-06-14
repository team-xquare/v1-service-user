package com.xquare.v1userservice.user.signin.spi

import java.util.UUID

interface AuthorityListSpi {
    suspend fun getAuthorities(userId: UUID): List<String>
}
