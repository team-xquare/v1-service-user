package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.configuration.validate.BadRequestException
import com.xquare.v1userservice.configuration.validate.RequestBodyValidator
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.UserApi
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.api.dtos.PointDomainResponse
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import com.xquare.v1userservice.user.router.dto.CreateUserRequest
import com.xquare.v1userservice.user.router.dto.GetUserDeviceTokenListResponse
import com.xquare.v1userservice.user.router.dto.GetUserPointResponse
import com.xquare.v1userservice.user.router.dto.GetUserResponse
import com.xquare.v1userservice.user.router.dto.SignInRequest
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

    suspend fun getUserDeviceTokensHandler(serverRequest: ServerRequest): ServerResponse {
        val userIds = serverRequest.queryParams()["users"]?.map { UUID.fromString(it) } ?: emptyList()

        val userDeviceTokenDomainResponse = userApi.getUserDeviceTokensByIdIn(userIds)
        val getUserDeviceTokenListResponse = userDeviceTokenDomainResponse.toGetUserDeviceTokenListResponse()
        return ServerResponse.ok().bodyValueAndAwait(getUserDeviceTokenListResponse)
    }

    private fun UserDeviceTokenResponse.toGetUserDeviceTokenListResponse() = GetUserDeviceTokenListResponse(
        tokens = this.tokens
    )

    suspend fun getUserPointHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = serverRequest.pathVariable("userId")
        val pointDomainResponse = userApi.getUserPointInformation(UUID.fromString(userId))
        val pointResponse = pointDomainResponse.toResponse()
        return ServerResponse.ok().bodyValueAndAwait(pointResponse)
    }

    private fun PointDomainResponse.toResponse() = GetUserPointResponse(
        name = this.userName,
        goodPoint = this.goodPoint,
        badPoint = this.badPoint
    )
}
