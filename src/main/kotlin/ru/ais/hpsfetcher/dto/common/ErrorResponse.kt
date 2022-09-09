package ru.ais.hpsfetcher.dto.common

import java.time.ZonedDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val error: String,
    val timestamp: ZonedDateTime = ZonedDateTime.now(),
    val traceId: String? = null
)
