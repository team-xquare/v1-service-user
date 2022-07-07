package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.TokenType
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.UserSignInApi
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.SignInResponse
import com.xquare.v1userservice.user.api.dtos.TokenRefreshResponse
import com.xquare.v1userservice.user.exceptions.PasswordNotMatchesException
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.exceptions.InvalidRefreshTokenException
import com.xquare.v1userservice.user.refreshtoken.exceptions.RefreshTokenNotFoundException
import com.xquare.v1userservice.user.refreshtoken.spi.RefreshTokenSpi
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
    private val authorityListSpi: AuthorityListSpi,
    private val refreshTokenSpi: RefreshTokenSpi
) : UserSignInApi {
    override suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): SignInResponse {
        val user = userRepositorySpi.findByAccountIdAndStateWithCreated(signInDomainRequest.accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        val deviceTokenModifiedUser =
            userRepositorySpi.applyChanges(user.setDeviceToken(signInDomainRequest.deviceToken))

        checkPasswordMatches(deviceTokenModifiedUser, signInDomainRequest.password)

        val params = buildAccessTokenParams(user)

        val accessToken =
            jwtTokenGeneratorSpi.generateJwtToken(signInDomainRequest.accountId, TokenType.ACCESS_TOKEN, params)
        val expireAt = LocalDateTime.now().plusHours(jwtTokenGeneratorSpi.getAccessTokenExpirationAsHour().toLong())

        val refreshToken = saveNewRefreshToken(user, params)

        return SignInResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.tokenValue,
            expireAt = expireAt
        )
    }

    private fun checkPasswordMatches(user: User, rawPassword: String) {
        val isPasswordMatches = passwordMatcherSpi.passwordMatches(rawPassword, user.password)

        if (!isPasswordMatches) {
            throw PasswordNotMatchesException(PasswordNotMatchesException.LOGIN_PASSWORD_NOT_MATCHES)
        }
    }

    override suspend fun userTokenRefresh(refreshToken: String): TokenRefreshResponse {
        val pureRefreshToken = refreshToken.split(" ").getOrNull(1)
            ?: throw InvalidRefreshTokenException("Invalid Refresh Token")

        val refreshTokenEntity = refreshTokenSpi.findByRefreshToken(pureRefreshToken)
            ?: throw RefreshTokenNotFoundException("Refresh Token Not Found")

        refreshTokenSpi.delete(refreshTokenEntity)

        val user = userRepositorySpi.findByIdAndStateWithCreated(refreshTokenEntity.userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        val params = buildAccessTokenParams(user)

        saveNewRefreshToken(user, params)

        val accessToken = jwtTokenGeneratorSpi.generateJwtToken(user.accountId, TokenType.ACCESS_TOKEN, params)

        val expireAt = LocalDateTime.now().plusHours(jwtTokenGeneratorSpi.getAccessTokenExpirationAsHour().toLong())

        return TokenRefreshResponse(
            accessToken = accessToken,
            expireAt = expireAt
        )
    }

    private suspend fun buildAccessTokenParams(user: User): MutableMap<String, Any> {
        val authorities = authorityListSpi.getAuthorities(user.id)

        return HashMap<String, Any>()
            .apply {
                put("authorities", authorities)
                put("role", user.role)
            }
    }

    private suspend fun saveNewRefreshToken(user: User, params: MutableMap<String, Any>): RefreshToken {
        val newRefreshToken = jwtTokenGeneratorSpi.generateJwtToken(user.accountId, TokenType.REFRESH_TOKEN, params)

        val refreshTokenDomain = RefreshToken(
            tokenValue = newRefreshToken,
            userId = user.id
        )

        return refreshTokenSpi.saveRefreshToken(refreshTokenDomain)
    }
}
