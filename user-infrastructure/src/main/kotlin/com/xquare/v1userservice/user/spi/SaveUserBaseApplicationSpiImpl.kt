package com.xquare.v1userservice.user.spi

import java.util.UUID
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class SaveUserBaseApplySpiImpl(
    private val webClient: WebClient,
    private val
) : SaveUserBaseApplySpi {
    override suspend fun processStep(userId: UUID) {
        webClient.post()
            .uri{ it.host()}
    }
}