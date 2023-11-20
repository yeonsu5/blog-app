package com.kotlin.blog.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val timestamp: LocalDateTime,
    val data: T? = null,
)

fun <T> response(
    httpStatus: HttpStatus,
    message: String,
    data: T? = null,
): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity(
        ApiResponse(
            code = httpStatus.value(),
            message = message,
            timestamp = LocalDateTime.now(),
            data = data,
        ),
        httpStatus,
    )
}
// responseEntity 이용 방법으로 개선
