package com.kotlin.blog.domain.vo

import java.time.LocalDateTime

data class PostViewVo(
    val id: Long,
    val title: String,
    val content: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
