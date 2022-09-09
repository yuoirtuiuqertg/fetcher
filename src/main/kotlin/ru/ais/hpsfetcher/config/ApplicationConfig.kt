package ru.ais.hpsfetcher.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.*
import javax.annotation.PostConstruct

@ConstructorBinding
@ConfigurationProperties("application")
data class ApplicationConfig (
    val timezone: String = "Europe/Moscow"
) {
    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone))
    }
}
