package com.kotlin.blog.service

import com.kotlin.blog.domain.Post
import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
) {
    // 전체 조회
    @Transactional(readOnly = true)
    fun getAllPosts(): List<PostResponse> {
        return postRepository.findAll().map { post -> PostResponse.of(post) }
    }

    // 1개 조회
    @Transactional(readOnly = true)
    fun getPostById(id: Long): PostResponse {
        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("post id $id not found")
        return PostResponse.of(post)
    }

    // 작성
    @Transactional
    fun savePost(request: PostSaveRequest) {
        val post = Post(request.title, request.content)
        postRepository.save(post)
    }

    // 수정
    @Transactional
    fun updatePost(id: Long, request: PostUpdateRequest): PostResponse {
        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("post id $id not found")
        post.update(request.title, request.content, request.updatedAt)
        return PostResponse.of(post)
    }

    // 삭제
    @Transactional
    fun deletePost(id: Long) {
        postRepository.deleteById(id)
    }
}
