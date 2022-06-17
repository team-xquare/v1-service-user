package com.xquare.v1userservice.user.signin.service

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.TokenType
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.exceptions.PasswordNotMatchesException
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import com.xquare.v1userservice.user.signin.api.UserSignInApi
import com.xquare.v1userservice.user.signin.spi.AuthorityListSpi
import com.xquare.v1userservice.user.signin.spi.JwtTokenGeneratorSpi
import com.xquare.v1userservice.user.signin.spi.PasswordMatcherSpi

@DomainService
class UserSignInApiImpl(
    private val userRepositorySpi: UserRepositorySpi,
    private val passwordMatcherSpi: PasswordMatcherSpi,
    private val jwtTokenGeneratorSpi: JwtTokenGeneratorSpi,
    private val authorityListSpi: AuthorityListSpi
) : UserSignInApi {
    override suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): User {
        val user = userRepositorySpi.findByAccountIdAndStateWithCreated(signInDomainRequest.accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        checkPasswordMatches(user, signInDomainRequest.password)
        val authorities = authorityListSpi.getAuthorities(user.id)

        val params = HashMap<String, Any>()
            .apply {
                put("authorities", authorities)
                put("role", user.role)
            }

        jwtTokenGeneratorSpi.generateJwtToken(signInDomainRequest.accountId, TokenType.ACCESS_TOKEN, params)
        return user
    }

    private fun checkPasswordMatches(user: User, rawPassword: String) {
        val isPasswordMatches = passwordMatcherSpi.passwordMatches(user.password, rawPassword)

        if (!isPasswordMatches) {
            throw PasswordNotMatchesException(PasswordNotMatchesException.LOGIN_PASSWORD_NOT_MATCHES)
        }
    }
}
