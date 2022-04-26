package com.xquare.v1userservice.user.handler

import com.xquare.v1userservice.configuration.validate.RequestBodyValidator
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.router.dto.saveuser.CreateUserRequest
import com.xquare.v1userservice.user.saveuser.api.CreateUserInPendingStateProcessor
import java.net.URI
import java.time.Year
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class UserHandler(
    private val createUserInPendingState: CreateUserInPendingStateProcessor,
    private val passwordEncoder: PasswordEncoder,
    private val requestBodyValidator: RequestBodyValidator
) {
    suspend fun saveUserHandler(serverRequest: ServerRequest): ServerResponse {
        val requestBody: CreateUserRequest = serverRequest.getCreateUserRequestBody()
        processSagaStep(requestBody)
        return ServerResponse.created(URI("/users")).buildAndAwait()
    }

    private suspend fun ServerRequest.getCreateUserRequestBody() =
        this.bodyToMono<CreateUserRequest>().awaitSingle()

    private suspend fun processSagaStep(createUserRequest: CreateUserRequest) {
        requestBodyValidator.validate(createUserRequest)
        val domainRequest = createUserRequest.toDomainUser()
        createUserInPendingState.processStep(domainRequest)
    }

    private suspend fun CreateUserRequest.toDomainUser() =
        User(
            password = passwordEncoder.encode(this.password),
            accountId = this.accountId,
            birthDay = this.birthDay,
            entranceYear = Year.now().minusYears(this.grade.toLong() - 1).value,
            profileFileName = this.profileImageUrl,
            num = this.num,
            grade = this.grade,
            name = this.name,
            classNum = this.classNum
        )
}
