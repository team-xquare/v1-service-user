package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.ApplicationRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseApplicationRequest
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@Repository
class SaveUserBaseApplicationSpiImpl(
    private val webClient: WebClient,
    @Value("\${service.application.host}")
    private val applicationHost: String,
    @Value("\${service.scheme}")
    private val scheme: String
) : SaveUserBaseApplicationProcessor, SaveUserBaseApplicationCompensator {
    override suspend fun processStep(userId: UUID) {
        val baseApplicationRequest = buildBaseApplicationRequest(userId)
        sendPostApplicationDefaultValue(baseApplicationRequest)
    }

    private fun buildBaseApplicationRequest(userId: UUID) =
        SaveUserBaseApplicationRequest(userId)

    private suspend fun sendPostApplicationDefaultValue(saveUserBaseApplicationRequest: SaveUserBaseApplicationRequest) =
        webClient.post()
            .uri {
                it.scheme(scheme)
                    .host(applicationHost)
                    .path("/applications/signup")
                    .build()
            }
            .bodyValue(saveUserBaseApplicationRequest)
            .retrieve()
            .onStatus(HttpStatus::isError) {
                throw ApplicationRequestFailedException("Failed request to save application default value", it.rawStatusCode())
            }
            .awaitBodilessEntity()

    override suspend fun revertStep(userId: UUID) {
        TODO("Not yet implemented")
    }
}
