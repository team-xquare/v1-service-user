package com.xquare.v1userservice.configuration.datasource

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource")
data class DatasourceProperties(
    val url: String,
    val username: String,
    val password: String,
    val showSql: Boolean,
    val formatSql: Boolean,
    val highlightSql: Boolean,
    val ddlAuto: String,
    val poolSize: Int
)
