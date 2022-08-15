package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.TokenResponse

interface UserSignInApi {
    suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): TokenResponse
    suspend fun userTokenRefresh(refreshToken: String): TokenResponse
}
