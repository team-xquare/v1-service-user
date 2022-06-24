package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.TokenType
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.UserSignInApi
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.SignInResponse
import com.xquare.v1userservice.user.exceptions.PasswordNotMatchesException
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.spi.AuthorityListSpi
import com.xquare.v1userservice.user.spi.JwtTokenGeneratorSpi
import com.xquare.v1userservice.user.spi.PasswordMatcherSpi
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import java.time.LocalDateTime

@DomainService
class UserSignInApiImpl(
    private val userRepositorySpi: UserRepositorySpi,
    private val passwordMatcherSpi: PasswordMatcherSpi,
    private val jwtTokenGeneratorSpi: JwtTokenGeneratorSpi,
    private val authorityListSpi: AuthorityListSpi
) : UserSignInApi {
    override suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): SignInResponse {
        val user = userRepositorySpi.findByAccountIdAndStateWithCreated(signInDomainRequest.accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        val deviceTokenModifiedUser = userRepositorySpi.applyChanges(user.setDeviceToken(signInDomainRequest.deviceToken))

        checkPasswordMatches(deviceTokenModifiedUser, signInDomainRequest.password)
        val authorities = authorityListSpi.getAuthorities(deviceTokenModifiedUser.id)

        val params = HashMap<String, Any>()
            .apply {
                put("authorities", authorities)
                put("role", deviceTokenModifiedUser.role)
            }

        val accessToken = jwtTokenGeneratorSpi.generateJwtToken(signInDomainRequest.accountId, TokenType.ACCESS_TOKEN, params)
        val refreshToken = jwtTokenGeneratorSpi.generateJwtToken(signInDomainRequest.accountId, TokenType.REFRESH_TOKEN, params)
        val expirationAt = LocalDateTime.now().plusHours(jwtTokenGeneratorSpi.getAccessTokenExpirationAsHour().toLong())

        return SignInResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expireAt = expirationAt
        )
    }

    private fun checkPasswordMatches(user: User, rawPassword: String) {
        val isPasswordMatches = passwordMatcherSpi.passwordMatches(user.password, rawPassword)

        if (!isPasswordMatches) {
            throw PasswordNotMatchesException(PasswordNotMatchesException.LOGIN_PASSWORD_NOT_MATCHES)
        }
    }
}
