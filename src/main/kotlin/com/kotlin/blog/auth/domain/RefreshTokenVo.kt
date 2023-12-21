package com.kotlin.blog.auth.domain

data class RefreshTokenVo(
    val userId: Long,
    var refreshToken: String,
    val id: Long,
)
