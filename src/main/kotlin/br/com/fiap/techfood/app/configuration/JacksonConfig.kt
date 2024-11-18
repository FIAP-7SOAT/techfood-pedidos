package br.com.fiap.techfood.app.configuration

import br.com.fiap.techfood.app.adapter.input.web.serializer.CategoryEnumDeserializer
import br.com.fiap.techfood.core.domain.enums.CategoryEnum
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            // Register existing modules
            registerKotlinModule() // From ObjectMapperConfig
            registerModule(JavaTimeModule()) // From ObjectMapperConfig

            // Add the custom deserializer module
            val module = SimpleModule().apply {
                addDeserializer(CategoryEnum::class.java, CategoryEnumDeserializer())
            }
            registerModule(module)

            // Configure other options
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}

