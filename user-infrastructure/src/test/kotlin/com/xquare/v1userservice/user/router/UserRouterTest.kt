package com.xquare.v1userservice.user.router

import com.ninjasquad.springmockk.MockkBean
import com.xquare.v1userservice.configuration.security.SecurityConfig
import com.xquare.v1userservice.user.handler.UserHandler
import com.xquare.v1userservice.user.router.dto.saveuser.CreateUserRequest
import io.mockk.ConstantAnswer
import io.mockk.coEvery
import java.net.URI
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.springframework.web.reactive.function.server.buildAndAwait
import reactor.kotlin.core.publisher.toMono

@ExperimentalCoroutinesApi
@WebFluxTest(UserRouter::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(SecurityConfig::class)
internal class UserRouterTest(
    private val webTestClient: WebTestClient
) {

    @MockkBean
    private lateinit var userHandler: UserHandler

    @Test
    fun userBaseRouter() = runTest {
        mockUserHandlerToReturnCreated()
        val request = buildSuccessRequest()
        val result = sendPostRequestWithBody(request)
        assertThat(result.status).isEqualTo(HttpStatus.CREATED)
    }

    private suspend fun mockUserHandlerToReturnCreated() =
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
            name = "test",
            authCode = "dsfadfs",
            birthDay = LocalDate.now(),
            grade = 3,
            classNum = 5,
            num = 20,
            profileImageUrl = "https://",
            password = "test"
        )
}
