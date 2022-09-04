package com.xquare.v1userservice.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "service")
class ServiceProperties(
    val baseHost: String
)
