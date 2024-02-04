package com.xquare.v1userservice.user.router

import com.xquare.v1userservice.configuration.extension.nullIfBlank
import com.xquare.v1userservice.configuration.validate.BadRequestException
import com.xquare.v1userservice.configuration.validate.RequestBodyValidator
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserRole
import com.xquare.v1userservice.user.aop.RequestHeaderAspect
import com.xquare.v1userservice.user.api.UserApi
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.api.dtos.ExcludeUserIdListResponse
import com.xquare.v1userservice.user.api.dtos.PointDomainResponse
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.TokenResponse
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import com.xquare.v1userservice.user.router.dto.CreateUserRequest
import com.xquare.v1userservice.user.router.dto.GetUserIdListRequest
import com.xquare.v1userservice.user.router.dto.GetTeacherInfoResponse
import com.xquare.v1userservice.user.router.dto.GetTeacherResponse
import com.xquare.v1userservice.user.router.dto.GetUserDeviceTokenListResponse
import com.xquare.v1userservice.user.router.dto.GetUserGradeClassNumListResponse
import com.xquare.v1userservice.user.router.dto.GetUserGradeClassNumResponse
import com.xquare.v1userservice.user.router.dto.GetUserInfoRequest
import com.xquare.v1userservice.user.router.dto.GetUserListResponse
import com.xquare.v1userservice.user.router.dto.GetUserNameResponse
import com.xquare.v1userservice.user.router.dto.GetUserPointResponse
import com.xquare.v1userservice.user.router.dto.GetUserProfileResponse
import com.xquare.v1userservice.user.router.dto.GetUserResponse
import com.xquare.v1userservice.user.router.dto.SignInRequest
import com.xquare.v1userservice.user.router.dto.UpdateProfileFileRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI
import java.util.UUID

@Component
class UserHandler(
    private val userApi: UserApi,
    private val requestBodyValidator: RequestBodyValidator,
    private val requestHeaderAspect: RequestHeaderAspect,
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
        val signInResponse: TokenResponse = userApi.userSignIn(domainRequest)
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

    suspend fun getUserByIdsToBodyHandler(serverRequest: ServerRequest): ServerResponse {
        val userIds = serverRequest.getUserInfoRequestBody()

        val users = userApi.getUsersByIdsIn(userIds.userIds)
        val userResponseDtos = users.map { it.toGetUserByAccountIdResponseDto() }
        val userListResponse = GetUserListResponse(userResponseDtos)
        return ServerResponse.ok().bodyValueAndAwait(userListResponse)
    }

    private suspend fun ServerRequest.getUserInfoRequestBody() =
        this.bodyToMono<GetUserInfoRequest>().awaitSingle()

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
            profileFileName = this.profileFileName ?: "",
            classNum = this.classNum,
            grade = this.grade,
            num = this.num,
            birthDay = this.birthDay,
            id = this.id,
            userRole = this.role
        )

    suspend fun getUserProfileHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = requestHeaderAspect.getUserId(serverRequest)
        val user = userApi.getUserById(userId)
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
        val userIds = serverRequest.getUserIdList().awaitSingle().userIdList

        val userDeviceTokenDomainResponse = userApi.getUserDeviceTokensByIdIn(userIds)
        val getUserDeviceTokenListResponse = userDeviceTokenDomainResponse.toGetUserDeviceTokenListResponse()
        return ServerResponse.ok().bodyValue(getUserDeviceTokenListResponse).awaitSingle()
    }

    private fun UserDeviceTokenResponse.toGetUserDeviceTokenListResponse() = GetUserDeviceTokenListResponse(
        tokens = this.tokens
    )

    suspend fun updateUserProfileFileNameHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = requestHeaderAspect.getUserId(serverRequest)
        val updateProfileFileRequest = serverRequest.getUpdateUserProfileFileRequestBody()
        userApi.updateProfileFileName(userId, updateProfileFileRequest.profileFileName)
        return ServerResponse.noContent().buildAndAwait()
    }

    private suspend fun ServerRequest.getUpdateUserProfileFileRequestBody() =
        this.bodyToMono<UpdateProfileFileRequest>().awaitSingle()

    suspend fun getUserPointHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = requestHeaderAspect.getUserId(serverRequest)
        val pointDomainResponse = userApi.getUserPointInformation(userId)
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
        val grade = serverRequest.queryParams().getFirst("grade")?.toIntOrNull()
            ?: throw BadRequestException("grade is required")
        val classNum = serverRequest.queryParams().getFirst("classNum")?.toIntOrNull()

        val users = userApi.getUserByGradeAndClass(grade, classNum)
        val userResponse = users.map { it.toGetUserGradeAndClass() }
        val userListResponse = GetUserGradeClassNumListResponse(userResponse)

        return ServerResponse.ok().bodyValueAndAwait(userListResponse)
    }

    private fun User.toGetUserGradeAndClass(): GetUserGradeClassNumResponse {
        return GetUserGradeClassNumResponse(
            id = this.id,
            profileFileName = this.profileFileName,
            num = "${this.grade}${this.classNum}${this.num.toString().padStart(2, '0')}",
            name = this.name
        )
    }

    suspend fun getAllStudentHandler(serverRequest: ServerRequest): ServerResponse {
        val users = userApi.getAllStudent()
        val userResponseDtos = users.map { it.toGetUserByAccountIdResponseDto() }
        val userListResponse = GetUserListResponse(userResponseDtos)

        return ServerResponse.ok().bodyValueAndAwait(userListResponse)
    }

    suspend fun getAllTeacherHandler(serverRequest: ServerRequest): ServerResponse {
        val teachers = userApi.getAllTeacher()
        val teacherInfoResponse = teachers.map { it.toGetTeacherInfoResponseDto() }
        val teacherResponse = GetTeacherResponse(teacherInfoResponse)

        return ServerResponse.ok().bodyValueAndAwait(teacherResponse)
    }

    suspend fun getAllStudentByNameHandler(serverRequest: ServerRequest): ServerResponse {
        val name = serverRequest.queryParam("name").orElse("")

        val users = userApi.getAllStudentByName(name)
        val userResponse = users.map { it.toGetUserNameResponseDto() }

        return ServerResponse.ok().bodyValueAndAwait(userResponse)
    }

    suspend fun getUserByRoleHandler(serverRequest: ServerRequest): ServerResponse {
        val role = serverRequest.queryParams().getFirst("roleName") ?: throw BadRequestException("roleName is required")
        checkUserRole(role)

        val users = userApi.getAllUserByRole(role)
        val userResponseDtos = users.map { it.toGetUserByAccountIdResponseDto() }
        val response = GetUserListResponse(userResponseDtos)

        return ServerResponse.ok().bodyValueAndAwait(response)
    }

    suspend fun logoutHandler(serverRequest: ServerRequest): ServerResponse {
        val userId = requestHeaderAspect.getUserId(serverRequest)
        userApi.setEmptyDeviceToken(userId)

        return ServerResponse.noContent().buildAndAwait()
    }

    private fun User.toGetUserNameResponseDto() =
        GetUserNameResponse(
            id = this.id,
            name = this.name,
            num = "${this.grade}${this.classNum}${this.num.toString().padStart(2, '0')}"
        )

    private fun User.toGetTeacherInfoResponseDto() =
        GetTeacherInfoResponse(
            id = this.id,
            name = this.name,
        )

    private fun checkUserRole(role: String?) {
        val isValidUserRole = UserRole.values().none { it.name == role }
        if (role != null && role != "" && isValidUserRole) {
            throw BadRequestException("roleName is invalid")
        }
    }

    suspend fun getExcludeUserListHandler(serverRequest: ServerRequest): ServerResponse {
        val excludeUserIds = serverRequest.getUserIdList().awaitSingle().userIdList.nullIfBlank()?.map { it }
        val users = userApi.getExcludeUserIdList(excludeUserIds)
        val response = ExcludeUserIdListResponse(users)

        return ServerResponse.ok().bodyValue(response).awaitSingle()
    }

    private suspend fun ServerRequest.getUserIdList() =
        this.bodyToMono<GetUserIdListRequest>()
}
