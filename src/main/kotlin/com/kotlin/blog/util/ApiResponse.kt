package com.kotlin.blog.util

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val timestamp: LocalDateTime,
    val data: T? = null,
) {
    companion object {
        fun <T> respond(
            code: HttpStatus,
            message: String,
            data: T? = null,
        ): ApiResponse<T> {
            return ApiResponse(
                code.value(),
                message,
                timestamp = LocalDateTime.now(),
                data,
            )
        }
    }
}
