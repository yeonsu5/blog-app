package com.kotlin.blog.controller

import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.service.PostService
import com.kotlin.blog.util.ApiResponse
import com.kotlin.blog.util.ExistenceCheck
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun findAll(): ApiResponse<List<PostListResponse>> {
        val allPosts = postService.getAllPosts()

        return ApiResponse.respond(HttpStatus.OK, "게시글 목록 조회", allPosts)
    }

    @ExistenceCheck
    @GetMapping("/posts/{id}")
    fun findPostById(@PathVariable id: Long): ApiResponse<PostResponse> {
        val post = postService.getPostById(id)

        return ApiResponse.respond(HttpStatus.OK, "게시글 상세 조회", post)
    }

    @PostMapping("/posts")
    fun createPost(@RequestBody request: PostSaveRequest): ApiResponse<PostResponse> {
        val savedPost = postService.savePost(request)

        return ApiResponse.respond(HttpStatus.CREATED, "게시글 작성", savedPost)
    }

    @ExistenceCheck
    @DeleteMapping("/posts/{id}")
    fun deletePost(@PathVariable id: Long): ApiResponse<String> {
        postService.deletePostById(id)

        return ApiResponse.respond(HttpStatus.OK, "게시글 삭제")
    }

    @ExistenceCheck
    @PutMapping("/posts/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody request: PostUpdateRequest): ApiResponse<PostResponse> {
        val updatedPost = postService.updatePost(id, request)

        return ApiResponse.respond(HttpStatus.OK, "게시글 수정", updatedPost)
    }
}
