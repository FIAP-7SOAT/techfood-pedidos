package br.com.fiap.techfood.app.configuration

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun v1(): OpenAPI = OpenAPI().info(buildInfo())

    private fun buildInfo(): Info =
        Info()
            .title("techfood-app")
}