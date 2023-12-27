package com.kotlin.blog.common.exception.handler

import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.common.exception.LoginFailureException
import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createErrorResponse
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationException(ex: MethodArgumentNotValidException):
        ResponseEntity<ApiResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()

        ex.bindingResult.allErrors.forEach { error -> // MethodArgumentNotValidException에 포함된 모든 유효성 검사 에러를 순회
            val fieldName = (error as FieldError).field // 각 에러에 대해 해당 필드 이름과
            val errorMessage = error.defaultMessage // 에러 메시지를 추출하여
            errors[fieldName] = errorMessage ?: "Not Exception Message" // 맵에 저장
        }
        return createErrorResponse(HttpStatus.BAD_REQUEST, data = errors)
    }

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException):
        ResponseEntity<ApiResponse<Map<String, String>>> {
        val error = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))

        return createErrorResponse(HttpStatus.BAD_REQUEST, data = error)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun illegalArgumentException(ex: IllegalArgumentException):
        ResponseEntity<ApiResponse<String>> {
        val error = ex.message ?: "Not Exception Message"
        return createErrorResponse(HttpStatus.BAD_REQUEST, data = error)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception):
        ResponseEntity<ApiResponse<Map<String, String>>> {
        val error = mapOf("미처리 예외" to (ex.message ?: "Not Exception Message"))

        return createErrorResponse(HttpStatus.BAD_REQUEST, data = error)
    }

    // JWT 토큰이 만료된 경우
    @ExceptionHandler(ExpiredJwtException::class)
    protected fun expiredJwtException():
        ResponseEntity<ApiResponse<String>> {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, data = "토큰이 만료되었습니다. 다시 로그인해 주세요.")
    }

    // 변조된 JWT 토큰을 사용하는 경우
    @ExceptionHandler(SignatureException::class)
    protected fun signatureException():
        ResponseEntity<ApiResponse<String>> {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, data = "토큰이 유효하지 않습니다.")
    }

    // JWT 토큰으로 아무 값이나 사용하는 경우
    @ExceptionHandler(MalformedJwtException::class)
    protected fun malformedJwtException():
        ResponseEntity<ApiResponse<String>> {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, data = "올바르지 않은 토큰입니다.")
    }

    // 로그인 실패 예외 - Custom exception
    @ExceptionHandler(LoginFailureException::class)
    protected fun loginFailureException():
        ResponseEntity<ApiResponse<String>> {
        return createErrorResponse(HttpStatus.BAD_REQUEST, data = "로그인에 실패하였습니다. 아이디와 비밀번호을 확인해주세요")
    }

    // JWT 토큰 없이 인증이 필요한 경로에 요청한 경우
    @ExceptionHandler(InsufficientAuthenticationException::class)
    protected fun insufficientAuthenticationException():
        ResponseEntity<ApiResponse<String>> {
        return createErrorResponse(HttpStatus.BAD_REQUEST, data = "인증이 필요한 요청입니다.")
    }
}
