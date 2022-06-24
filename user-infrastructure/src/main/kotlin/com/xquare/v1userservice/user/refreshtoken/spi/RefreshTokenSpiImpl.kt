package com.xquare.v1userservice.user.refreshtoken.spi

import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.exceptions.RefreshTokenSaveFailedException
import com.xquare.v1userservice.user.refreshtoken.mapper.RefreshTokenDomainMapper
import java.time.Duration
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenSpiImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>,
    private val refreshTokenDomainMapper: RefreshTokenDomainMapper
) : RefreshTokenSpi {
    override suspend fun saveRefreshToken(refreshToken: RefreshToken): RefreshToken {
        val refreshTokenEntity = refreshTokenDomainMapper.refreshTokenDomainToEntity(refreshToken)
        val isSaveSuccess = reactiveRedisOperations.opsForValue()
            .set(refreshToken.tokenValue, refreshTokenEntity, Duration.ofDays(14)).awaitSingle()

        if (!isSaveSuccess) {
            throw RefreshTokenSaveFailedException("Refresh token save failed")
        }

        return refreshToken
    }
}
