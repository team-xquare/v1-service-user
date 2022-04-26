package com.xquare.v1userservice.user

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.annotations.SagaStep
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackageClasses = [User::class],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = [DomainService::class, SagaStep::class]
        )
    ]
)
class UserConfiguration
