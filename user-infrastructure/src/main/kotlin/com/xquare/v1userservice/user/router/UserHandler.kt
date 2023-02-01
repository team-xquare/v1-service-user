package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.configuration.validate.BadRequestException
import com.xquare.v1userservice.configuration.validate.RequestBodyValidator
import com.xquare.v1userservice.jwt.UnAuthorizedException
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.UserApi
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.api.dtos.PointDomainResponse
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import com.xquare.v1userservice.user.router.dto.CreateUserRequest
import com.xquare.v1userservice.user.router.dto.GetUserDeviceTokenListResponse
import com.xquare.v1userservice.user.router.dto.GetUserGradeAndClassListResponse
import com.xquare.v1userservice.user.router.dto.GetUserGradeAndClassResponse
import com.xquare.v1userservice.user.router.dto.GetUserListResponse
import com.xquare.v1userservice.user.router.dto.GetUserPointResponse
import com.xquare.v1userservice.user.router.dto.GetUserProfileResponse
import com.xquare.v1userservice.user.router.dto.GetUserResponse
import com.xquare.v1userservice.user.router.dto.SignInRequest
import com.xquare.v1userservice.user.router.dto.UpdateProfileFileRequest
import java.net.URI
import java.util.UUID
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class UserHandler(
    private val userApi: UserApi,
    private val requestBodyValidator: RequestBodyValidator
) {
    suspend fun saveUserHandler(serverRequest: ServerRequest): ServerResponse {
        val requestBody: CreateUserRequest = serverRequest.getCreateUserRequestBody()
        requestBodyValidator.validate(requestBody)

        val domainRequest = requestBody.toDomainRequest()
        userApi.saveUser(domainRequest)

        return ServerResponse.created(URI("/users")).buildAndAwait()
    }

    private suspend fun ServerRequest.getCreateUserRequestBody() =
        this.bodyToMono<CreateUserRequest>().awaitSingle()

    private fun CreateUserRequest.toDomainRequest() = CreatUserDomainRequest(
        accountId = this.accountId,
        verificationCode = this.verificationCode,
        profileFileName = this.profileFileName,
        password = this.password
    )

    suspend fun userSignInHandler(serverRequest: ServerRequest): ServerResponse {
        val signInRequest = serverRequest.getSignInRequestBody()
        val domainRequest = signInRequest.toDomainRequest()
        val signInResponse = userApi.userSignIn(domainRequest)
        return ServerResponse.ok().bodyValueAndAwait(signInResponse)
    }

    private suspend fun ServerRequest.getSignInRequestBody() =
        this.bodyToMono<SignInRequest>().awaitSingle()

    private fun SignInRequest.toDomainRequest() =
        SignInDomainRequest(
            accountId = this.accountId,
            password = this.password,
            deviceToken = this.deviceToken
        )

    suspend fun userTokenRefreshHandler(serverRequest: ServerRequest): ServerResponse {
        val refreshToken = serverRequest.headers().firstHeader("Refresh-Token")
            ?: throw BadRequestException("Refresh-Token is required")

        val signInResponse = userApi.userTokenRefresh(refreshToken)
        return ServerResponse.ok().bodyValueAndAwait(signInResponse)
    }

    suspend fun getUserByIdHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = serverRequest.pathVariable("userId")
        val user = userApi.getUserById(UUID.fromString(userId))
        val userResponseDto = user.toGetUserByAccountIdResponseDto()
        return ServerResponse.ok().bodyValueAndAwait(userResponseDto)
    }

    suspend fun getUserByIdsInHandler(serverRequest: ServerRequest): ServerResponse {
        val userIds = serverRequest.queryParams()["userId"]?.map { UUID.fromString(it) }
            ?: throw BadRequestException("userId is required")
        val users = userApi.getUsersByIdsIn(userIds)
        val userResponseDtos = users.map { it.toGetUserByAccountIdResponseDto() }
        val userListResponse = GetUserListResponse(userResponseDtos)
        return ServerResponse.ok().bodyValueAndAwait(userListResponse)
    }

    suspend fun getUserByAccountIdHandler(serverRequest: ServerRequest): ServerResponse {
        val accountId = serverRequest.pathVariable("accountId")
        val user = userApi.getUserByAccountId(accountId)
        val userResponseDto = user.toGetUserByAccountIdResponseDto()
        return ServerResponse.ok().bodyValueAndAwait(userResponseDto)
    }

    private fun User.toGetUserByAccountIdResponseDto() =
        GetUserResponse(
            accountId = this.accountId,
            password = this.password,
            name = this.name,
            profileFileName = this.profileFileName,
            classNum = this.classNum,
            grade = this.grade,
            num = this.num,
            birthDay = this.birthDay,
            id = this.id
        )

    suspend fun getUserProfileHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = serverRequest.headers().firstHeader("Request-User-Id") ?: throw UnAuthorizedException("UnAuthorized")
        val user = userApi.getUserById(UUID.fromString(userId))
        val userProfileResponseDto = user.toGetUserProfileResponseDto()
        return ServerResponse.ok().bodyValueAndAwait(userProfileResponseDto)
    }

    private fun User.toGetUserProfileResponseDto() =
        GetUserProfileResponse(
            accountId = this.accountId,
            name = this.name,
            birthDay = this.birthDay,
            grade = this.grade,
            classNum = this.classNum,
            num = this.num,
            profileFileName = this.profileFileName
        )

    suspend fun getUserDeviceTokensHandler(serverRequest: ServerRequest): ServerResponse {
        val userIds = serverRequest.queryParams()["users"]?.map { UUID.fromString(it) } ?: emptyList()

        val userDeviceTokenDomainResponse = userApi.getUserDeviceTokensByIdIn(userIds)
        val getUserDeviceTokenListResponse = userDeviceTokenDomainResponse.toGetUserDeviceTokenListResponse()
        return ServerResponse.ok().bodyValueAndAwait(getUserDeviceTokenListResponse)
    }

    private fun UserDeviceTokenResponse.toGetUserDeviceTokenListResponse() = GetUserDeviceTokenListResponse(
        tokens = this.tokens
    )

    suspend fun updateUserProfileFileNameHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = serverRequest.headers().firstHeader("Request-User-Id") ?: throw UnAuthorizedException("UnAuthorized")
        val updateProfileFileRequest = serverRequest.getUpdateUserProfileFileRequestBody()
        userApi.updateProfileFileName(UUID.fromString(userId), updateProfileFileRequest.profileFileName)
        return ServerResponse.noContent().buildAndAwait()
    }

    private suspend fun ServerRequest.getUpdateUserProfileFileRequestBody() =
        this.bodyToMono<UpdateProfileFileRequest>().awaitSingle()

    suspend fun getUserPointHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = serverRequest.headers().firstHeader("Request-User-Id") ?: throw UnAuthorizedException("UnAuthorized")
        val pointDomainResponse = userApi.getUserPointInformation(UUID.fromString(userId))
        val pointResponse = pointDomainResponse.toResponse()
        return ServerResponse.ok().bodyValueAndAwait(pointResponse)
    }

    private fun PointDomainResponse.toResponse() = GetUserPointResponse(
        name = this.userName,
        goodPoint = this.goodPoint,
        badPoint = this.badPoint,
        profileFileName = this.profileFileName
    )

    suspend fun getUserByGradeAndClassHandler(serverRequest: ServerRequest): ServerResponse {
        val grade = grade(serverRequest.queryParams().getFirst("grade"))
        val classNum = classNum(serverRequest.queryParams().getFirst("classNum"))
        val users = userApi.getUserByGradeAndClass(grade, classNum)
        val userResponse = users.map { it.toGetUserGradeAndClass() }
        val userListResponse = GetUserGradeAndClassListResponse(userResponse)
        return ServerResponse.ok().bodyValueAndAwait(userListResponse)
    }

    private fun User.toGetUserGradeAndClass(): GetUserGradeAndClassResponse {
        val num = if (this.num < 9) "0${this.num}" else this.num.toString()

        return GetUserGradeAndClassResponse(
            id = this.id,
            profileFileName = this.profileFileName,
            num = "${this.grade}${this.classNum}${num}",
            name = this.name
        )
    }

    private fun grade(grade: String?): Int {
        return if (grade == null || grade == "") {
            throw BadRequestException("grade is required")
        } else grade.toInt()
    }

    private fun classNum(classNum: String?): Int {
        return if (classNum == null || classNum == "") 0 else classNum.toInt()
    }
}
