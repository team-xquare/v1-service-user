package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.AuthorityRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.AuthorityListResponse
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class AuthorityListSpiImpl(
    private val webClient: WebClient,
    @Value("\${service.authority.host}")
    private val authorityHost: String,
    @Value("\${service.scheme}")
    private val scheme: String
) : AuthorityListSpi {
    override suspend fun getAuthorities(userId: UUID): List<String> {
        val clientResponse = sendGetAuthoritiesRequest(userId)
        return getAuthorityNamesFromResponse(clientResponse)
    }

    private suspend fun sendGetAuthoritiesRequest(userId: UUID): WebClient.ResponseSpec {
        return webClient.get().uri { uri ->
            uri.scheme(scheme)
                .host(authorityHost)
                .path("/authorities/access-management/{userId}")
                .build(userId)
        }.retrieve()
            .onStatus(HttpStatus::isError) {
                throw AuthorityRequestFailedException("Request failed to get authorities", it.rawStatusCode())
            }
    }

    private suspend fun getAuthorityNamesFromResponse(clientResponse: WebClient.ResponseSpec): List<String> {
        val authorityListResponse = clientResponse.awaitBody<AuthorityListResponse>()
        return authorityListResponse.authorityList.map { it.authorityName }
    }
}
