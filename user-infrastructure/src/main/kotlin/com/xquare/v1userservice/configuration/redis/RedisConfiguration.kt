package com.xquare.v1userservice.configuration.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {
    @Primary
    @Bean
    fun redisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Any> {
        val jsonSerializer = GenericJackson2JsonRedisSerializer()
        val stringSerializer = StringRedisSerializer()

        val serializationContext = RedisSerializationContext.newSerializationContext<String, Any>()
            .key(stringSerializer)
            .value(jsonSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}
