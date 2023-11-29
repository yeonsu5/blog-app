package com.kotlin.blog.common.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ApiResponse<T>(
    val httpStatusCode: Int,
    val result: String = Result.SUCCESS.name,
    val message: String = Result.SUCCESS.message,
    val data: T? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

fun <T> createResponse(
    httpStatus: HttpStatus,
    message: String = Result.SUCCESS.message,
    data: T? = null,
): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity(
        ApiResponse(
            httpStatusCode = httpStatus.value(),
            message = message,
            data = data,
        ),
        httpStatus,
    )
}

enum class Result(val message: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다."),
}
