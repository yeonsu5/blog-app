package com.kotlin.blog.service

import com.kotlin.blog.domain.Post
import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.repository.PostRepository
import com.kotlin.blog.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : PostService {
    // 전체 조회
    @Transactional(readOnly = true)
    override fun getAllPosts(): List<PostListResponse> {
        return postRepository.findAll().map { post -> PostListResponse.of(post) }
    }

    // 1개 조회
    @Transactional(readOnly = true)
    override fun getPostById(id: Long): PostResponse {
        val post = postRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("post id $id not found") // 예외처리 AOP 적용하기 - Bean 설정
        return PostResponse.of(post)
    }

    // 작성
    @Transactional
    override fun savePost(request: PostSaveRequest): PostResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw IllegalArgumentException("user id ${request.userId} not found")
        Post(request.title, request.content, user)
        val savedPost = postRepository.save(Post(request.title, request.content, user))
        return PostResponse.of(savedPost)
    }

    // 수정
    @Transactional
    override fun updatePost(id: Long, request: PostUpdateRequest): PostResponse {
        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("post id $id not found")
        post.update(request.title, request.content, request.updatedAt)
        return PostResponse.of(post)
    }

    // 삭제
    @Transactional
    override fun deletePostById(id: Long) {
        postRepository.deleteById(id)
    }
}
