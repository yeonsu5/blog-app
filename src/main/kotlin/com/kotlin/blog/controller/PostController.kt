package com.kotlin.blog.controller

import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PostController(
    private val postService: PostService,
) {

    // validation 적용 - 컨트롤러
    @GetMapping("/api/posts")
    fun findAll(): ResponseEntity<List<PostListResponse>> {
        val allPosts = postService.getAllPosts()
        return ResponseEntity.ok()
            .body(allPosts)
    }

    // id의 존재 여부에 대해서 validation
    @GetMapping("/api/posts/{id}")
    fun findPostById(@PathVariable id: Long): ResponseEntity<PostResponse> {
        val post = postService.getPostById(id)
        return ResponseEntity.ok()
            .body(post)
    }

    @PostMapping("/api/posts")
    fun createPost(@RequestBody request: PostSaveRequest): ResponseEntity<PostResponse> {
        val savedPost = postService.savePost(request)
        return ResponseEntity.ok()
            .body(savedPost)
    }

    @DeleteMapping("/api/posts/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<String> {
        postService.deletePostById(id)
        return ResponseEntity.ok()
            .body("게시글 삭제 완료")
    }

    @PutMapping("/api/posts/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody request: PostUpdateRequest): ResponseEntity<PostResponse> {
        val updatedPost = postService.updatePost(id, request)
        return ResponseEntity.ok()
            .body(updatedPost)
    }
}
