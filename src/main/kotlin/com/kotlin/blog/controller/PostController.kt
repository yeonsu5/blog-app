package com.kotlin.blog.controller

import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.service.PostService
import com.kotlin.blog.util.ExistenceCheck
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun findAll(): ResponseEntity<List<PostListResponse>> {
        val allPosts = postService.getAllPosts()

        return ResponseEntity.ok()
            .body(allPosts)
    }

    @ExistenceCheck
    @GetMapping("/posts/{id}")
    fun findPostById(@PathVariable id: Long): ResponseEntity<PostResponse> {
        val post = postService.getPostById(id)

        return ResponseEntity.ok()
            .body(post)
    }

    @PostMapping("/posts")
    fun createPost(@RequestBody request: PostSaveRequest): ResponseEntity<PostResponse> {
        val savedPost = postService.savePost(request)

        return ResponseEntity.ok()
            .body(savedPost)
    }

    @ExistenceCheck
    @DeleteMapping("/posts/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<String> {
        postService.deletePostById(id)

        return ResponseEntity.ok()
            .body("게시글 삭제 완료")
    }

    @ExistenceCheck
    @PutMapping("/posts/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody request: PostUpdateRequest): ResponseEntity<PostResponse> {
        val updatedPost = postService.updatePost(id, request)

        return ResponseEntity.ok()
            .body(updatedPost)
    }
}
