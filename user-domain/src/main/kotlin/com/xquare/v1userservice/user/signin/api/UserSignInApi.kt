package com.xquare.v1userservice.user.signin.api

import com.xquare.v1userservice.user.signin.service.SignInDomainRequest
import com.xquare.v1userservice.user.signin.service.SignInResponse

interface UserSignInApi {
    suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): SignInResponse
}
