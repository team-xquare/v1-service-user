package com.xquare.v1userservice.user.api

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.api.dtos.PointDomainResponse
import com.xquare.v1userservice.user.api.dtos.SignInDomainRequest
import com.xquare.v1userservice.user.api.dtos.TokenResponse
import com.xquare.v1userservice.user.api.dtos.UserDeviceTokenResponse
import java.util.UUID

interface UserApi {
    suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User
    suspend fun getUserById(userId: UUID): User
    suspend fun getUsersByIdsIn(userIds: List<UUID>): List<User>
    suspend fun getUserByAccountId(accountId: String): User
    suspend fun userSignIn(signInDomainRequest: SignInDomainRequest): TokenResponse
    suspend fun userTokenRefresh(refreshToken: String): TokenResponse
    suspend fun getUserDeviceTokensByIdIn(idList: List<UUID>): UserDeviceTokenResponse
    suspend fun updateProfileFileName(userId: UUID, profileFileName: String?)
    suspend fun getUserPointInformation(userId: UUID): PointDomainResponse
    suspend fun getUserByGradeAndClass(grade: Int, classNum: Int?): List<User>
    suspend fun getAllUser(): List<User>
}
