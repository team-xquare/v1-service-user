package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.authority.AuthorityProperties
import com.xquare.v1userservice.user.spi.dtos.SaveUserBaseAuthorityRequest
import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class SaveUserBaseAuthoritySpiImpl(
    private val webClient: WebClient,
    private val authorityProperties: AuthorityProperties
) : SaveUserBaseAuthoritySpi {
    override suspend fun processStep(userId: UUID) {
        val request = SaveUserBaseAuthorityRequest(userId)
        sendSaveUserBaseAuthorityRequest(request)
    }

    private fun sendSaveUserBaseAuthorityRequest(saveUserBaseAuthorityRequest: SaveUserBaseAuthorityRequest): WebClient.ResponseSpec {
        return webClient.post()
            .uri {
                it.host(authorityProperties.host)
                    .path("/authorities/access-management")
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(saveUserBaseAuthorityRequest)
            .retrieve()
    }
}
