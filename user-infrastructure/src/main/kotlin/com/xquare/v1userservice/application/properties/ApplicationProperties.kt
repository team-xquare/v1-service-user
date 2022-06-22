package com.xquare.v1userservice.application.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "service.application")
class ApplicationProperties(
    val host: String
)
