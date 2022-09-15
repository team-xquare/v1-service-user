package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.AuthorityRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseAuthorityRequest
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@Repository
class SaveUserBaseAuthoritySpiImpl(
    private val webClient: WebClient,
    @Value("\${service.authority.host}")
    private val authorityHost: String
) : SaveUserBaseAuthorityProcessor, SaveUserBaseAuthorityCompensator {
    override suspend fun processStep(userId: UUID): Result<Unit> {
        return try {
            val request = SaveUserBaseAuthorityRequest(userId)
            sendSaveUserBaseAuthorityRequest(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun sendSaveUserBaseAuthorityRequest(saveUserBaseAuthorityRequest: SaveUserBaseAuthorityRequest) {
        webClient.post()
            .uri {
                it.scheme("http")
                    .host(authorityHost)
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
