package com.xquare.v1userservice.user.refreshtoken.spi

import com.xquare.v1userservice.jwt.properties.RefreshTokenProperties
import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.RefreshTokenEntity
import com.xquare.v1userservice.user.refreshtoken.exceptions.RefreshTokenSaveFailedException
import com.xquare.v1userservice.user.refreshtoken.mapper.RefreshTokenDomainMapper
import java.time.Duration
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.expireAndAwait
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenSpiImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>,
    private val refreshTokenDomainMapper: RefreshTokenDomainMapper,
    private val refreshTokenProperties: RefreshTokenProperties
) : RefreshTokenSpi {
    override suspend fun saveRefreshToken(refreshToken: RefreshToken): RefreshToken {
        val refreshTokenEntity = refreshTokenDomainMapper.refreshTokenDomainToEntity(refreshToken)
        val refreshTokenExpiration = Duration.ofHours(refreshTokenProperties.expirationAsHour.toLong())

        val isSaveSuccess = reactiveRedisOperations.opsForValue()
            .set(refreshToken.tokenValue, refreshTokenEntity, refreshTokenExpiration).awaitSingle()

        if (!isSaveSuccess) {
            throw RefreshTokenSaveFailedException("Refresh token save failed")
        }

        return refreshToken
    }

    override suspend fun findByRefreshToken(refreshToken: String): RefreshToken? {
        val refreshTokenEntity = reactiveRedisOperations.opsForValue().get(refreshToken).awaitSingleOrNull()
            as? RefreshTokenEntity

        return refreshTokenEntity?.let { refreshTokenDomainMapper.refreshTokenEntityToDomain(refreshTokenEntity) }
    }

    override suspend fun updateExpiredAt(refreshToken: RefreshToken) {
        val refreshTokenExpiration = Duration.ofHours(refreshTokenProperties.expirationAsHour.toLong())
        val isUpdateExpireSuccess = reactiveRedisOperations.expireAndAwait(refreshToken.tokenValue, refreshTokenExpiration)

        if (!isUpdateExpireSuccess) {
            throw RefreshTokenSaveFailedException("Refresh token Modify Failed")
        }
    }
}
