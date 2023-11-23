package com.kotlin.blog.controller

import com.kotlin.blog.domain.vo.PostSaveVo
import com.kotlin.blog.domain.vo.PostUpdateVo
import com.kotlin.blog.dto.request.OrderBy
import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.request.SortBy
import com.kotlin.blog.dto.request.SortingRequest
import com.kotlin.blog.dto.response.PostListResponse
import com.kotlin.blog.dto.response.PostResponse
import com.kotlin.blog.service.PostService
import com.kotlin.blog.util.ApiResponse
import com.kotlin.blog.util.ExistenceCheck
import com.kotlin.blog.util.response
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "sortBy", required = false) sortBy: SortBy?,
        @RequestParam(value = "orderBy", required = false) orderBy: OrderBy?,
    ): ResponseEntity<ApiResponse<Page<PostListResponse>>> {
        val sortingRequest = SortingRequest(sortBy ?: SortBy.ID, orderBy ?: OrderBy.DESC)

        val allPosts = postService.getAllPosts(page, sortingRequest).map { postVo ->
            PostListResponse.VotoDto(postVo)
        }

        return response(HttpStatus.OK, "게시글 목록 조회", allPosts)
    }

    @ExistenceCheck
    @GetMapping("/posts/{id}")
    fun findPostById(@PathVariable id: Long): ResponseEntity<ApiResponse<PostResponse>> {
        val postById = postService.getPostById(id)

        val post = PostResponse.VotoDto(postById)

        return response(HttpStatus.OK, "게시글 상세 조회", post)
    }

    @PostMapping("/posts")
    fun createPost(@RequestBody request: PostSaveRequest): ResponseEntity<ApiResponse<PostResponse>> {
        // request를 vo로 변환
        val postSaveVo = PostSaveVo(request.title, request.content, request.userId)

        postService.savePost(postSaveVo)

        return response(HttpStatus.CREATED, "게시글 작성")
    }

    @ExistenceCheck
    @DeleteMapping("/posts/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<ApiResponse<String>> {
        postService.deletePostById(id)

        return response(HttpStatus.OK, "게시글 삭제")
    }

    @ExistenceCheck
    @PutMapping("/posts/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody request: PostUpdateRequest):
        ResponseEntity<ApiResponse<PostResponse>> {
        val postUpdateVo = PostUpdateVo(id, request.title, request.content)

        postService.updatePost(postUpdateVo)

        return response(HttpStatus.OK, "게시글 수정")
    }
}
