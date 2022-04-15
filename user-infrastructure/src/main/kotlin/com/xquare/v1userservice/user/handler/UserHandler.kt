package com.xquare.v1userservice.user.handler

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.api.saveuser.SaveUserApi
import com.xquare.v1userservice.user.router.dto.saveuser.CreateUserRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI
import java.time.Year

@Component
class UserHandler(
    private val saveUserApi: SaveUserApi,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun saveUserHandler(serverRequest: ServerRequest): ServerResponse {
        val requestBody: CreateUserRequest = serverRequest.getCreateUserRequestBody()
        val domainRequest = requestBody.toDomainUser()

        saveUserApi.createNewUserAndStartSagaOrchestration(domainRequest)
        return ServerResponse.created(URI("/users")).buildAndAwait()
    }

    private suspend fun ServerRequest.getCreateUserRequestBody() =
        this.bodyToMono<CreateUserRequest>().awaitSingle()

    private suspend fun CreateUserRequest.toDomainUser() =
        User(
            password = passwordEncoder.encode(this.password),
            accountId = this.accountId,
            birthDay = this.birthDay,
            entranceYear = Year.now().minusYears(this.grade.toLong() - 1),
            profileFileName = this.profileImageUrl,
            num = this.num,
            grade = this.grade,
            name = this.name,
            classNum = this.classNum
        )
}
