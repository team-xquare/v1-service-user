package com.xquare.v1userservice.configuration.security

import com.xquare.v1userservice.user.UserRole
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.PATCH
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    private val STUDENT = "ROLE_" + UserRole.STU
    private val SCHOOL = "ROLE_" + UserRole.SCH
    private val DOMITORY = "ROLE_" + UserRole.DOR

    @Bean
    protected fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .cors().disable()
            .authorizeExchange()
            .pathMatchers(POST, "/", "/login").permitAll()
            .pathMatchers(PUT, "/login").permitAll()
            .pathMatchers(PATCH, "/").hasAnyRole(STUDENT)
            .anyExchange().hasAnyRole(SCHOOL, DOMITORY)
            .and().build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
