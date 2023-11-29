package com.kotlin.blog.post.dto.request

import java.time.LocalDateTime

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
