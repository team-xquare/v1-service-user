package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.spi.dtos.GitResponse
import java.util.*

interface GitSpi {
    suspend fun getIsGitConnected(): Boolean
}