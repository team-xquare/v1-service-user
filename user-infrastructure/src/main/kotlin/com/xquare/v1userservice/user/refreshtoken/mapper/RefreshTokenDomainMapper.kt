package com.xquare.v1userservice.user.refreshtoken.mapper

import com.xquare.v1userservice.user.refreshtoken.RefreshToken
import com.xquare.v1userservice.user.refreshtoken.RefreshTokenEntity
import org.mapstruct.Mapper

@Mapper
interface RefreshTokenDomainMapper {
    fun refreshTokenDomainToEntity(refreshToken: RefreshToken): RefreshTokenEntity
    fun refreshTokenEntityToDomain(refreshTokenEntity: RefreshTokenEntity): RefreshToken
}
