package com.xquare.v1userservice.user.spi

interface GitSpi {
    suspend fun getIsGitConnected(): Boolean
}
