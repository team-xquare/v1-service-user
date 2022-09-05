package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.user.exceptions.PointRequestFailedException
import com.xquare.v1userservice.user.spi.dtos.PointResponse
import com.xquare.v1userservice.user.spi.dtos.UserPointResponse
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class PointSpiImpl(
    private val webClient: WebClient,
    @Value("\${service.point.host}")
    private val pointHost: String
) : PointSpi {
    override suspend fun getUserPoint(userId: UUID): PointResponse {
        return webClient.get().uri {
            it.scheme("http")
                .host(pointHost)
                .path("/point/{userId}")
                .build(userId)
        }.retrieve()
            .onStatus(HttpStatus::isError) {
                throw PointRequestFailedException("Failed request to get user point", it.rawStatusCode())
            }
            .awaitBody<UserPointResponse>().let {
                PointResponse(
                    goodPoint = it.goodPoint,
                    badPoint = it.badPoint
                )
            }
    }
}
