package com.kotlin.blog.post.dto.response

import com.kotlin.blog.post.domain.vo.PostListViewVo
import com.kotlin.blog.post.domain.vo.PostSearchViewVo
import java.time.LocalDateTime

data class PostListResponse(
    val title: String,
    val userNickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val id: Long,
) {

    companion object {
        fun voToDto(postListViewVo: PostListViewVo): PostListResponse {
            return PostListResponse(
                id = postListViewVo.id,
                title = postListViewVo.title,
                userNickname = postListViewVo.userNickname,
                createdAt = postListViewVo.createdAt,
                updatedAt = postListViewVo.updatedAt,
            )
        }
        fun voToDto(postSearchViewVo: PostSearchViewVo): PostListResponse {
            return PostListResponse(
                id = postSearchViewVo.id,
                title = postSearchViewVo.title,
                userNickname = postSearchViewVo.userNickname,
                createdAt = postSearchViewVo.createdAt,
                updatedAt = postSearchViewVo.updatedAt,
            )
        }
    }
}
