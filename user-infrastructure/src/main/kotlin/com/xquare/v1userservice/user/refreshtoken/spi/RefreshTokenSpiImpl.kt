package com.xquare.v1userservice.user.refreshtoken.spi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.xquare.v1userservice.jwt.properties.JwtProperties
import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.RefreshTokenEntity
import com.xquare.v1userservice.user.refreshtoken.exceptions.RefreshTokenSaveFailedException
import com.xquare.v1userservice.user.refreshtoken.mapper.RefreshTokenDomainMapper
import java.time.Duration
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenSpiImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>,
    private val refreshTokenDomainMapper: RefreshTokenDomainMapper,
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper
) : RefreshTokenSpi {
    override suspend fun saveRefreshToken(refreshToken: RefreshToken): RefreshToken {
        val refreshTokenEntity = refreshTokenDomainMapper.refreshTokenDomainToEntity(refreshToken)
        val refreshTokenExpiration = Duration.ofHours(jwtProperties.refreshTokenProperties.expirationAsHour.toLong())

        val isSaveSuccess = reactiveRedisOperations.opsForValue()
            .set(refreshToken.tokenValue, refreshTokenEntity, refreshTokenExpiration).awaitSingle()

        if (!isSaveSuccess) {
            throw RefreshTokenSaveFailedException("Refresh token save failed")
        }

        return refreshToken
    }

    override suspend fun findByRefreshToken(refreshToken: String): RefreshToken? {
        val refreshTokenFromDB = reactiveRedisOperations.opsForValue().get(refreshToken).awaitSingleOrNull()
        val refreshTokenEntity = refreshTokenFromDB?.let { objectMapper.convertValue<RefreshTokenEntity>(it) }

        return refreshTokenEntity?.let { refreshTokenDomainMapper.refreshTokenEntityToDomain(it) }
    }

    override suspend fun delete(refreshToken: RefreshToken) {
        reactiveRedisOperations.delete(refreshToken.tokenValue).awaitSingle()
    }
}
