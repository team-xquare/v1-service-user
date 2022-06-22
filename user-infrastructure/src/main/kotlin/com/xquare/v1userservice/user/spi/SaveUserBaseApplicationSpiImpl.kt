package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.application.properties.ApplicationProperties
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseApplicationRequest
import java.util.UUID
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class SaveUserBaseApplicationSpiImpl(
    private val webClient: WebClient,
    private val applicationProperties: ApplicationProperties
) : SaveUserBaseApplicationSpi {
    override suspend fun processStep(userId: UUID) {
        val baseApplicationRequest = buildBaseApplicationRequest(userId)

        sendPostApplicationDefaultValue(baseApplicationRequest)
    }

    private fun buildBaseApplicationRequest(userId: UUID) =
        SaveUserBaseApplicationRequest(userId)

    private fun sendPostApplicationDefaultValue(saveUserBaseApplicationRequest: SaveUserBaseApplicationRequest) =
        webClient.post()
            .uri {
                it.host(applicationProperties.host)
                    .path("/applications/signup")
                    .build()
            }
            .bodyValue(saveUserBaseApplicationRequest)
            .retrieve()
}