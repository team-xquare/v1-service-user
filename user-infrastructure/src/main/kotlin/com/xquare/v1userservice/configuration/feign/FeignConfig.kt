package com.xquare.v1userservice.configuration.feign

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Configuration

@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration::class)
@Configuration
class FeignConfig
