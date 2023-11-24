package com.kotlin.blog.dto.response

import com.kotlin.blog.domain.vo.PostListViewVo
import com.kotlin.blog.domain.vo.PostSearchViewVo
import java.time.LocalDateTime

data class PostListResponse(
    val title: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val id: Long,
) {

    companion object {
        fun voToDto(postListViewVo: PostListViewVo): PostListResponse {
            return PostListResponse(
                id = postListViewVo.id,
                title = postListViewVo.title,
                authorName = postListViewVo.authorName,
                createdAt = postListViewVo.createdAt,
                updatedAt = postListViewVo.updatedAt,
            )
        }
        fun voToDto(postSearchViewVo: PostSearchViewVo): PostListResponse {
            return PostListResponse(
                id = postSearchViewVo.id,
                title = postSearchViewVo.title,
                authorName = postSearchViewVo.authorName,
                createdAt = postSearchViewVo.createdAt,
                updatedAt = postSearchViewVo.updatedAt,
            )
        }
    }
}
