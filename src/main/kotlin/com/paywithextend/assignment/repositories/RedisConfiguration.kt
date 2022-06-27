package com.paywithextend.assignment.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.paywithextend.assignment.models.UserModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.text.DateFormat
import java.util.UUID


@Configuration
class RedisConfiguration {

    private val objectMapper = Jackson2ObjectMapperBuilder().indentOutput(true)
                                                            .modules(JavaTimeModule(),
                                                                     KotlinModule.Builder().build())
        /* Tradeoff: Nice for dev, not so much for prod */  .dateFormat(DateFormat.getDateInstance(DateFormat.LONG))
                                                            .build<ObjectMapper>()

    @Bean
    fun redisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<UUID, UserModel> =
        ReactiveRedisTemplate(
            factory,
            RedisSerializationContext.newSerializationContext<UUID, UserModel>(StringRedisSerializer())
                                     .key(Jackson2JsonRedisSerializer(UUID::class.java))
                                     .value(
                                         Jackson2JsonRedisSerializer(UserModel::class.java)
                                             .also { it.setObjectMapper(objectMapper) } )
                                     .build())
}
