package com.xquare.v1userservice.feign.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "server")
@ConstructorBinding
data class ExcelProperties (
    val scheme: String,
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String
)
