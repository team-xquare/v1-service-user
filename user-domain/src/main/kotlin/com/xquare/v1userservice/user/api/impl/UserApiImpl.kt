package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.TokenType
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserRole
import com.xquare.v1userservice.user.api.CreateUserInPendingStateCompensator
import com.xquare.v1userservice.user.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.api.UserApi
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.api.dtos.PointDomainResponse
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.TokenResponse
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import com.xquare.v1userservice.user.exceptions.PasswordNotMatchesException
import com.xquare.v1userservice.user.exceptions.UserAlreadyExistsException
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.exceptions.InvalidRefreshTokenException
import com.xquare.v1userservice.user.refreshtoken.exceptions.RefreshTokenNotFoundException
import com.xquare.v1userservice.user.refreshtoken.spi.RefreshTokenSpi
import com.xquare.v1userservice.user.spi.AuthorityListSpi
import com.xquare.v1userservice.user.spi.JwtTokenGeneratorSpi
import com.xquare.v1userservice.user.spi.PasswordEncoderSpi
import com.xquare.v1userservice.user.spi.PasswordMatcherSpi
import com.xquare.v1userservice.user.spi.PointSpi
import com.xquare.v1userservice.user.spi.SaveUserBaseApplicationCompensator
import com.xquare.v1userservice.user.spi.SaveUserBaseApplicationProcessor
import com.xquare.v1userservice.user.spi.SaveUserBaseAuthorityCompensator
import com.xquare.v1userservice.user.spi.SaveUserBaseAuthorityProcessor
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.exceptions.VerificationCodeNotFoundException
import com.xquare.v1userservice.user.verificationcode.spi.VerificationCodeSpi
import java.time.LocalDateTime
import java.util.UUID

@DomainService
class UserApiImpl(
    private val createUserInPendingStateProcessor: CreateUserInPendingStateProcessor,
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor,
    private val saveUserBaseAuthorityProcessor: SaveUserBaseAuthorityProcessor,
    private val verificationCodeSpi: VerificationCodeSpi,
    private val passwordEncoderSpi: PasswordEncoderSpi,
    private val userRepositorySpi: UserRepositorySpi,
    private val createUserInPendingStateCompensator: CreateUserInPendingStateCompensator,
    private val saveUserBaseAuthorityCompensator: SaveUserBaseAuthorityCompensator,
    private val saveUserBaseApplicationProcessor: SaveUserBaseApplicationProcessor,
    private val saveUserBaseApplicationCompensator: SaveUserBaseApplicationCompensator,
    private val jwtTokenGeneratorSpi: JwtTokenGeneratorSpi,
    private val refreshTokenSpi: RefreshTokenSpi,
    private val authorityListSpi: AuthorityListSpi,
    private val passwordMatcherSpi: PasswordMatcherSpi,
    private val pointSpi: PointSpi
) : UserApi {
    override suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User {
        val verificationCode: VerificationCode = verificationCodeSpi.getByCode(creatUserDomainRequest.verificationCode)
            ?: throw VerificationCodeNotFoundException("Verification Code Not Found")
        checkIsAccountIdAlreadyExists(creatUserDomainRequest.accountId)
        checkIsUsed(verificationCode.isUsed)
        val domainUser = verificationCode.toStudentUser(creatUserDomainRequest)
        val savedUser = createUserInPendingStateProcessor.processStep(domainUser)

        runCatching {
            saveUserBaseAuthorityProcessor.processStep(savedUser.id)
        }.onFailure {
            saveUserBaseAuthorityCompensator.revertStep(savedUser.id)
            createUserInPendingStateCompensator.revertStep(savedUser.id)
            throw it
        }

        runCatching {
            saveUserBaseApplicationProcessor.processStep(savedUser.id)
        }.onFailure {
            saveUserBaseApplicationCompensator.revertStep(savedUser.id)
            saveUserBaseAuthorityCompensator.revertStep(savedUser.id)
            createUserInPendingStateCompensator.revertStep(savedUser.id)
            throw it
        }

        updateUserCreatedStateStepProcessor.processStep(savedUser.id)
        pointSpi.saveUserPointStatus(savedUser.id)

        return savedUser
    }

    private fun VerificationCode.toStudentUser(creatUserDomainRequest: CreatUserDomainRequest) = User(
        name = this.userName,
        entranceYear = this.entranceYear,
        birthDay = this.birthDay,
        grade = this.grade,
        role = UserRole.STU,
        num = this.num,
        classNum = this.classNum,
        password = passwordEncoderSpi.encodeString(creatUserDomainRequest.password),
        accountId = creatUserDomainRequest.accountId,
        profileFileName = creatUserDomainRequest.profileFileName
    )

    private suspend fun checkIsAccountIdAlreadyExists(accountId: String) {
        userRepositorySpi.findByAccountIdAndStateWithCreated(accountId)
            ?.let { throw UserAlreadyExistsException("$accountId Already Exists") }
    }

    private suspend fun checkIsUsed(isUsed: Boolean) {
        if (isUsed) {
            throw UserAlreadyExistsException("User Is Used")
        }
    }

    override suspend fun getUserDeviceTokensByIdIn(idList: List<UUID>): UserDeviceTokenResponse {
        val userDeviceTokens = userRepositorySpi.findAllByIdIn(idList)
            .filter { it.deviceToken != null }
            .map { it.deviceToken!! }

        return UserDeviceTokenResponse(userDeviceTokens)
    }

    override suspend fun updateProfileFileName(userId: UUID, profileFileName: String?) {
        val user = userRepositorySpi.findByIdAndStateWithCreated(userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
        val updatedUser = user.updateProfileFileName(profileFileName)
        userRepositorySpi.applyChanges(updatedUser)
    }

    override suspend fun getUserById(userId: UUID): User {
        return userRepositorySpi.findByIdAndStateWithCreated(userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
    }

    override suspend fun getUsersByIdsIn(userIds: List<UUID>): List<User> {
        return userRepositorySpi.findAllByIdIn(userIds)
    }

    override suspend fun getUserByAccountId(accountId: String): User {
        return userRepositorySpi.findByAccountIdAndStateWithCreated(accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
    }

    override suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): TokenResponse {
        val user = userRepositorySpi.findByAccountIdAndStateWithCreated(signInDomainRequest.accountId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        val deviceTokenModifiedUser =
            userRepositorySpi.applyChanges(user.setDeviceToken(signInDomainRequest.deviceToken))

        checkPasswordMatches(deviceTokenModifiedUser, signInDomainRequest.password)

        val params = buildAccessTokenParams(user)

        val accessToken =
            jwtTokenGeneratorSpi.generateJwtToken(user.id.toString(), TokenType.ACCESS_TOKEN, params)
        val expireAt = LocalDateTime.now().plusHours(jwtTokenGeneratorSpi.getAccessTokenExpirationAsHour().toLong())

        val refreshToken = saveNewRefreshToken(user, params)

        return TokenResponse(
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

    override suspend fun userTokenRefresh(refreshToken: String): TokenResponse {
        val pureRefreshToken = refreshToken.split(" ").getOrNull(1)
            ?: throw InvalidRefreshTokenException("Invalid Refresh Token")

        val refreshTokenEntity = refreshTokenSpi.findByRefreshToken(pureRefreshToken)
            ?: throw RefreshTokenNotFoundException("Refresh Token Not Found")

        refreshTokenSpi.delete(refreshTokenEntity)

        val user = userRepositorySpi.findByIdAndStateWithCreated(refreshTokenEntity.userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)
        refreshTokenSpi.delete(refreshTokenEntity)

        val params = buildAccessTokenParams(user)

        val refreshTokenDomain = saveNewRefreshToken(user, params)
        val accessToken = jwtTokenGeneratorSpi.generateJwtToken(user.id.toString(), TokenType.ACCESS_TOKEN, params)

        val expireAt = LocalDateTime.now().plusHours(jwtTokenGeneratorSpi.getAccessTokenExpirationAsHour().toLong())

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshTokenDomain.tokenValue,
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
        val newRefreshToken = jwtTokenGeneratorSpi.generateJwtToken(user.id.toString(), TokenType.REFRESH_TOKEN, params)

        val refreshTokenDomain = RefreshToken(
            tokenValue = newRefreshToken,
            userId = user.id
        )

        return refreshTokenSpi.saveRefreshToken(refreshTokenDomain)
    }

    override suspend fun getUserPointInformation(userId: UUID): PointDomainResponse {
        val user = userRepositorySpi.findByIdAndStateWithCreated(userId)
            ?: throw UserNotFoundException(UserNotFoundException.USER_ID_NOT_FOUND)

        val userPoint = pointSpi.getUserPoint(userId)

        return PointDomainResponse(
            goodPoint = userPoint.goodPoint,
            badPoint = userPoint.badPoint,
            userName = user.name,
            profileFileName = user.profileFileName
        )
    }

    override suspend fun getUserByGradeAndClass(grade: Int, classNum: Int?): List<User> {
        return userRepositorySpi.findAllByGradeAndClass(grade, classNum)
    }
}
