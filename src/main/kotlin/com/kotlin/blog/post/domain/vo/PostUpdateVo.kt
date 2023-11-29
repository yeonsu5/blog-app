package com.kotlin.blog.post.domain.vo

import java.time.LocalDateTime

data class PostUpdateVo(
    val id: Long,
    val title: String,
    val content: String,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
