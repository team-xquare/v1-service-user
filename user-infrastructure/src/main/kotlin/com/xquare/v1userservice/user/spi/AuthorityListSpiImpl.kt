package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.authority.AuthorityProperties
import com.xquare.v1userservice.user.spi.dtos.AuthorityListResponse
import java.util.UUID
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class AuthorityListSpiImpl(
    private val webClient: WebClient,
    private val authorityProperties: AuthorityProperties
) : AuthorityListSpi {
    override suspend fun getAuthorities(userId: UUID): List<String> {
        val clientResponse = sendGetAuthoritiesRequest(userId)
        return getAuthorityNamesFromResponse(clientResponse)
    }

    private suspend fun sendGetAuthoritiesRequest(userId: UUID): WebClient.ResponseSpec {
        return webClient.get().uri { uri ->
            uri.scheme("http")
                .host(authorityProperties.host)
                .path("/authorities/{userId}")
                .build(userId)
        }.retrieve()
    }

    private suspend fun getAuthorityNamesFromResponse(clientResponse: WebClient.ResponseSpec): List<String> {
        val authorityListResponse = clientResponse.awaitBody<AuthorityListResponse>()
        return authorityListResponse.authorities.map { it.authorityName }
    }
}
