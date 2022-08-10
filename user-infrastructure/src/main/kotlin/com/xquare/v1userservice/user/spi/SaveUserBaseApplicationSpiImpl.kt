package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.application.properties.ApplicationProperties
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseApplicationRequest
import java.util.UUID
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

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

    private suspend fun sendPostApplicationDefaultValue(saveUserBaseApplicationRequest: SaveUserBaseApplicationRequest) =
        webClient.post()
            .uri {
                it.scheme("https")
                    .host(applicationProperties.host)
                    .path("/applications/signup")
                    .build()
            }
            .bodyValue(saveUserBaseApplicationRequest)
            .retrieve().awaitBodilessEntity()
}
