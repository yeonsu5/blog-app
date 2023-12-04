package com.kotlin.blog.user.dto.request

import jakarta.validation.constraints.NotBlank

data class UserLoginRequest(

    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,

)
