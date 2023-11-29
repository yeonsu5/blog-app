package com.kotlin.blog.post.domain.vo

import java.time.LocalDateTime

data class PostSaveVo(
    val title: String,
    val content: String,
    val userId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
