package com.xquare.v1userservice.authority

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "service.authority")
class AuthorityProperties(
    val host: String,
    val getAuthorityEndpoint: String
)
