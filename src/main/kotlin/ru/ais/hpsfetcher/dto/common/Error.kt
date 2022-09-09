package ru.ais.hpsfetcher.dto.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.client.HttpStatusCodeException
import java.time.ZonedDateTime

data class Error(
    val status: Int,
    val error: String? = null,
    val message: String? = null,
    val timestamp: ZonedDateTime = ZonedDateTime.now()
)

fun HttpStatusCodeException.getError(): Error =
    jacksonObjectMapper().readValue(responseBodyAsString, Error::class.java)
