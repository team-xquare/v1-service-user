package com.xquare.v1userservice.user.refreshtoken.spi

import com.xquare.v1userservice.user.refreshtoken.RefreshToken

interface RefreshTokenSpi {
    suspend fun saveRefreshToken(refreshToken: RefreshToken): RefreshToken
}
