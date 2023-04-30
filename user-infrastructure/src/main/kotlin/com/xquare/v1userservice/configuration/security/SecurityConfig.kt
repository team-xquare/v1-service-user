package com.xquare.v1userservice.configuration.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    protected fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .cors().disable()
            .authorizeExchange()
            .pathMatchers(POST, "/users", "/users/login").permitAll()
            .pathMatchers(PUT, "/users/login").permitAll()
            .anyExchange().authenticated()
            .and().build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
