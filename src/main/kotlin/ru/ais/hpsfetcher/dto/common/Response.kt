package ru.ais.hpsfetcher.dto.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@JsonInclude(JsonInclude.Include.NON_NULL)
class Response<T_RESULT>(
    @JsonUnwrapped
    var result: T_RESULT? = null
) {
    @JsonUnwrapped
    var error: Error? = null

    fun error(
        status: Int,
        error: String? = null,
        message: String? = null
    ): Response<T_RESULT> {
        this.result = null
        this.error = Error(status, error, message)
        return this
    }
}

fun <T> successResponse(data: T): ResponseEntity<Response<T>> {
    return ResponseEntity.ok(Response(data))
}

fun <T> errorResponse(
    httpStatus: HttpStatus,
    error: String? = null,
    message: String? = null
): ResponseEntity<Response<T>> {
    return ResponseEntity<Response<T>>(
        Response<T>().apply {
            error(httpStatus.value(), error, message)
        }, httpStatus
    )
}

fun <T> errorResponse(
    httpStatus: Int,
    error: String? = null,
    message: String? = null
): ResponseEntity<Response<T>> {
    return ResponseEntity<Response<T>>(
        Response<T>().apply {
            error(httpStatus, error, message)
        }, HttpStatus.valueOf(httpStatus)
    )
}
