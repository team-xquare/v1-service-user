package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.AuthorityRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.SaveUserTestAuthorityRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import java.util.*

@Repository
class SaveUserTestAuthoritySpiImpl(
    private val webClient: WebClient,
    @Value("\${service.authority.host}")
    private val authorityHost: String,
    @Value("\${service.scheme}")
    private val scheme: String
) : SaveUserTestAuthorityProcessor {
    override suspend fun processStep(userId: UUID) {
        val request = SaveUserTestAuthorityRequest(userId, listOf("TEST"))
        sendSaveUserBaseAuthorityRequest(request)
    }

    private suspend fun sendSaveUserBaseAuthorityRequest(saveUserTestAuthorityRequest: SaveUserTestAuthorityRequest) {
        webClient.post()
            .uri {
                it.scheme(scheme)
                    .host(authorityHost)
                    .path("/authorities/access-management")
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(saveUserTestAuthorityRequest)
            .retrieve().onStatus(HttpStatus::isError) {
                throw AuthorityRequestFailedException("Request failed to save authorities", it.rawStatusCode())
            }.awaitBodilessEntity()
    }
}
