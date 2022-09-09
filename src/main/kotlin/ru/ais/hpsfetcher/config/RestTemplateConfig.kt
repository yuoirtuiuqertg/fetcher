package ru.ais.hpsfetcher.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "http-conf.timeout")
data class RestTemplateConfig(
    val connect: Long = 5,
    val read: Long = 5,
)

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate(config: RestTemplateConfig): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(config.connect))
        .setReadTimeout(Duration.ofSeconds(config.read))
        .additionalMessageConverters()
//        .errorHandler(JsonResponseErrorHandler(objectMapper))
        .build()
}
