package com.xquare.v1userservice.user.signin.api

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.signin.service.SignInDomainRequest

interface UserSignInApi {
    suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): User
}
