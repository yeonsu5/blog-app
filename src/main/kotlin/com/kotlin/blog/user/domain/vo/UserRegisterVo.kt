package com.kotlin.blog.user.domain.vo

import com.kotlin.blog.user.domain.entity.User
import java.time.LocalDateTime

data class UserRegisterVo(
    val email: String,
    val password: String?,
    val nickname: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toEntity(): User {
        return User(
            email = this.email,
            password = this.password,
            nickname = this.nickname,
            createdAt = this.createdAt,
        )
    }
}
