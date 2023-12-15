package com.kotlin.blog.common.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ApiResponse<T>(
    val httpStatusCode: Int,
    val result: String,
    val message: String,
    val data: T? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

fun <T> createResponse(
    httpStatus: HttpStatus,
    result: String = Result.SUCCESS.name,
    message: String = Result.SUCCESS.message,
    data: T? = null,
): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity(
        ApiResponse(
            httpStatusCode = httpStatus.value(),
            result = result,
            message = message,
            data = data,
        ),
        httpStatus,
    )
}

fun <T> createErrorResponse(
    httpStatus: HttpStatus,
    result: String = Result.ERROR.name,
    message: String = Result.ERROR.message,
    data: T? = null,
): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity(
        ApiResponse(
            httpStatusCode = httpStatus.value(),
            result = result,
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
