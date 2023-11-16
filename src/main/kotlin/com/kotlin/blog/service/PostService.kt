package com.kotlin.blog.service

import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse

interface PostService {
    // 전체 조회
    fun getAllPosts(): List<PostListResponse>

    // 1개 조회
    fun getPostById(id: Long): PostResponse

    // 작성
    fun savePost(request: PostSaveRequest): PostResponse

    // 수정
    fun updatePost(id: Long, request: PostUpdateRequest): PostResponse

    // 삭제
    fun deletePostById(id: Long)
}
