package com.kotlin.blog.service

import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse

interface PostService {
    fun getAllPosts(): List<PostListResponse>
    fun getPostById(id: Long): PostResponse
    fun savePost(request: PostSaveRequest): PostResponse
    fun updatePost(id: Long, request: PostUpdateRequest): PostResponse
    fun deletePostById(id: Long)
}
