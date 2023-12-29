package com.kotlin.blog.user.domain.vo

import java.time.LocalDateTime

data class UserListVo(
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val id: Long,
)
