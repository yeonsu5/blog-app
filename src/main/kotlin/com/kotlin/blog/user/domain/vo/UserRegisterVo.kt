package com.kotlin.blog.user.domain.vo

import java.time.LocalDateTime

data class UserRegisterVo(
    val email: String,
    val password: String,
    val nickname: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
