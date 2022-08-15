package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.SignInResponse

interface UserSignInApi {
    suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): SignInResponse
    suspend fun userTokenRefresh(refreshToken: String): SignInResponse
}
