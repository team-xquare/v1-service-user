package com.xquare.v1userservice.configuration.property

import com.xquare.v1userservice.BASE_PACKAGE
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@Configuration
class ConfigurationPropertyConfig
