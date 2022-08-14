package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.authority.AuthorityProperties
import com.xquare.v1userservice.user.exceptions.AuthorityRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseAuthorityRequest
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@Repository
class SaveUserBaseAuthoritySpiImpl(
    private val webClient: WebClient,
    private val authorityProperties: AuthorityProperties,
) : SaveUserBaseAuthorityProcessor, SaveUserBaseAuthorityCompensator {
    override suspend fun processStep(userId: UUID) {
        val request = SaveUserBaseAuthorityRequest(userId)
        sendSaveUserBaseAuthorityRequest(request)
    }

    private suspend fun sendSaveUserBaseAuthorityRequest(saveUserBaseAuthorityRequest: SaveUserBaseAuthorityRequest) {
        webClient.post()
            .uri {
                it.scheme("http")
                    .host(authorityProperties.host)
                    .path("/authorities/access-management/basic")
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(saveUserBaseAuthorityRequest)
            .retrieve().onStatus(HttpStatus::isError) {
                throw AuthorityRequestFailedException("Request failed to save authorities", it.rawStatusCode())
            }.awaitBodilessEntity()
    }

    override suspend fun revertStep(userId: UUID) {
        TODO("Not yet implemented")
    }
}
