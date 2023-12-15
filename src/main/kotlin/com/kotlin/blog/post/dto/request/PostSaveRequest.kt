package com.kotlin.blog.post.dto.request

import jakarta.validation.constraints.NotBlank

data class PostSaveRequest(
    @field:NotBlank(message = "제목은 공백이 아니어야 합니다")
    val title: String,

    @field:NotBlank(message = "내용은 공백이 아니어야 합니다")
    val content: String,

    val userId: Long,
)
