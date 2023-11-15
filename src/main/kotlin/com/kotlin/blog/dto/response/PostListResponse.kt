package com.kotlin.blog.dto.response

import com.kotlin.blog.domain.Post
import java.time.LocalDateTime

data class PostListResponse(
    val title: String,
    val author: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val id: Long,
) {

    companion object {
        fun of(post: Post): PostListResponse {
            return PostListResponse(
                title = post.title,
                author = post.author.nickname,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
                id = post.id!!,
            )
        }
    }
}
