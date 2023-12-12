package com.kotlin.blog.user.dto.request

import com.kotlin.blog.common.exception.InvalidInputException
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserRegisterRequest(

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해 주세요",
    )
    val password: String,

    @field:NotBlank
    val confirmPassword: String,

    @field:NotBlank
    val nickname: String,
) {
    init {
        require(password == confirmPassword) {
            throw InvalidInputException("password", "비밀번호와 확인 비밀번호가 일치하지 않습니다.")
        }
    }
}
