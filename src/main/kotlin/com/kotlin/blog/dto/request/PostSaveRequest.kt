package com.kotlin.blog.dto.request

data class PostSaveRequest(
    val title: String,
    val content: String,
    val userId: Long,
)
