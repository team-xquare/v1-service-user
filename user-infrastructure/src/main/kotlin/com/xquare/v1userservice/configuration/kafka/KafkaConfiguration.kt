package com.xquare.v1userservice.configuration.kafka

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@Configuration
class KafkaConfiguration {
    @Bean
    fun kafkaCoroutineScopeIO() =
        CoroutineScope(Dispatchers.IO)

    @Bean
    fun kafkaStringJsonConverter() =
        StringJsonMessageConverter()
}