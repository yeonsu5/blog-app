package com.kotlin.blog.post.dto.request

data class PostSaveRequest(
    val title: String,
    val content: String,
    val userId: Long,
)
