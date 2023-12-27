package com.kotlin.blog.user.domain.vo

import com.kotlin.blog.user.domain.entity.Role
import java.time.LocalDateTime

data class UserRegisterVo(
    val email: String,
    val password: String?,
    val nickname: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val role: Role = Role.USER, // 하드코딩
)
