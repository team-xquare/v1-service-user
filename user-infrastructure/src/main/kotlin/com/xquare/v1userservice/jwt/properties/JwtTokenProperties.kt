package com.xquare.v1userservice.jwt.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
data class JwtProperties(
    val secretKey: String,
    @NestedConfigurationProperty
    val accessTokenProperties: AccessTokenProperties,
    @NestedConfigurationProperty
    val refreshTokenProperties: RefreshTokenProperties
) {
    fun getAccessTokenExpirationAsHour() =
        this.accessTokenProperties.expirationAsHour.toLong()

    fun getRefreshTokenExpirationAsHour() =
        this.refreshTokenProperties.expirationAsHour.toLong()
}

interface BaseTokenProperties {
    val expirationAsHour: Int
}

@ConstructorBinding
data class AccessTokenProperties(
    override val expirationAsHour: Int
) : BaseTokenProperties

@ConstructorBinding
data class RefreshTokenProperties(
    override val expirationAsHour: Int
) : BaseTokenProperties
