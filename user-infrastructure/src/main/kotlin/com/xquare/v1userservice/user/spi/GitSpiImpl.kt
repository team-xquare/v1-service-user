package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.GitRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.GitResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class GitSpiImpl(
    private val webClient: WebClient,
    @Value("https://api.xquare.app")
    private val gitHost: String,
    @Value("\${service.scheme}")
    private val scheme: String
) : GitSpi {
    override suspend fun getIsGitConnected(): Boolean {
        val clientResponse = sendGetGitRequest()
        return getGitFromResponse(clientResponse)
    }

    private suspend fun sendGetGitRequest(): WebClient.ResponseSpec {
        return webClient.get().uri { uri ->
            uri.scheme(scheme)
                .host(gitHost)
                .path("/git/exist")
                .build()
        }.retrieve()
            .onStatus(HttpStatus::isError) {
                throw GitRequestFailedException("Request failed to get git", it.rawStatusCode())
            }
    }

    private suspend fun getGitFromResponse(clientResponse: WebClient.ResponseSpec): Boolean{
        val gitResponse = clientResponse.awaitBody<GitResponse>().isConnected
        return gitResponse
    }
}
