package com.xquare.v1userservice.user.router

import com.ninjasquad.springmockk.MockkBean
import com.xquare.v1userservice.configuration.security.SecurityConfig
import com.xquare.v1userservice.user.api.dtos.TokenResponse
import com.xquare.v1userservice.user.router.dto.CreateUserRequest
import com.xquare.v1userservice.user.router.dto.GetUserResponse
import com.xquare.v1userservice.user.router.dto.SignInRequest
import io.mockk.ConstantAnswer
import io.mockk.coEvery
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import reactor.kotlin.core.publisher.toMono

@ExperimentalCoroutinesApi
@WebFluxTest(UserRouter::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(SecurityConfig::class)
internal class UserRouterTest(
    private val webTestClient: WebTestClient
) {

    companion object {
        private const val ACCESS_TOKEN_SUCCESS_VALUE = "access.token.value"
        private const val REFRESH_TOKEN_SUCCESS_VALUE = "refresh.token.value"
    }

    @MockkBean
    private lateinit var userHandler: UserHandler

    @Test
    fun userSignUp() = runTest {
        mockUserServiceToReturnUser()
        val request = buildSuccessRequest()
        val result = sendPostRequestWithBody(request)
        assertThat(result.status).isEqualTo(HttpStatus.CREATED)
    }

    private suspend fun mockUserServiceToReturnUser() =
        coEvery { userHandler.saveUserHandler(any()) }
            .answers(ConstantAnswer(ServerResponse.created(URI("/users")).buildAndAwait()))

    private fun sendPostRequestWithBody(body: CreateUserRequest) =
        webTestClient.post()
            .uri("/users")
            .body<CreateUserRequest>(body.toMono())
            .exchange()
            .returnResult<Void>()

    private fun buildSuccessRequest() =
        CreateUserRequest(
            accountId = "tes12t",
            verificationCode = "dsfadfs",
            profileFileName = "https://",
            password = "test"
        )

    @Test
    fun userSignIn() = runTest {
        mockUserHandlerToReturnUserSignInSuccess()

        val request = buildUserSignInRequest()
        val result = sendSignInRequestWithBody(request)
        val responseBody = result.responseBody.awaitSingle()

        assertThat(responseBody.accessToken).isEqualTo(ACCESS_TOKEN_SUCCESS_VALUE)
        assertThat(responseBody.refreshToken).isEqualTo(REFRESH_TOKEN_SUCCESS_VALUE)
    }

    private suspend fun mockUserHandlerToReturnUserSignInSuccess() {
        val response = buildUserSignInResponse()
        coEvery { userHandler.userSignInHandler(any()) }
            .answers(ConstantAnswer(ServerResponse.ok().bodyValueAndAwait(response)))
    }

    private fun buildUserSignInResponse() = TokenResponse(
        accessToken = ACCESS_TOKEN_SUCCESS_VALUE,
        refreshToken = REFRESH_TOKEN_SUCCESS_VALUE,
        expireAt = LocalDateTime.now().plusHours(2)
    )

    private fun buildUserSignInRequest() = SignInRequest(
        accountId = "test",
        password = "test",
        deviceToken = "test"
    )

    private fun sendSignInRequestWithBody(signInRequest: SignInRequest) =
        webTestClient.post()
            .uri("/users/login")
            .body<SignInRequest>(signInRequest.toMono())
            .exchange()
            .returnResult<TokenResponse>()

    @Test
    fun getUserById() = runTest {
        mockUserHandlerToReturnUserInformation()

        val result = sendGetUserRequest()
        val responseBody = result.responseBody.awaitSingle()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
        assertThat(responseBody.id).isNotNull()
    }

    private suspend fun mockUserHandlerToReturnUserInformation() {
        val response = buildGetUserResponse()
        coEvery { userHandler.getUserByIdHandler(any()) }
            .answers(ConstantAnswer(ServerResponse.ok().bodyValueAndAwait(response)))
    }

    private fun sendGetUserRequest() =
        webTestClient.get()
            .uri("/users/id/${UUID.randomUUID()}")
            .exchange()
            .returnResult<GetUserResponse>()

    private fun buildGetUserResponse() = GetUserResponse(
        id = UUID.randomUUID(),
        accountId = "accountId",
        name = "name",
        birthDay = LocalDate.now(),
        grade = 3,
        classNum = 1,
        num = 20,
        profileFileName = "https://",
        password = "password"
    )
}
