package com.kotlin.blog.post.service

import com.kotlin.blog.post.domain.vo.PostListViewVo
import com.kotlin.blog.post.domain.vo.PostSaveVo
import com.kotlin.blog.post.domain.vo.PostSearchViewVo
import com.kotlin.blog.post.domain.vo.PostUpdateVo
import com.kotlin.blog.post.domain.vo.PostViewVo
import com.kotlin.blog.post.dto.request.SortingRequest
import org.springframework.data.domain.Page

interface PostService {
    // 전체 조회
    fun getAllPosts(page: Int, sortingRequest: SortingRequest): Page<PostListViewVo>

    // 1개 조회
    fun getPostById(id: Long): PostViewVo

    // 작성
    fun savePost(postSaveVo: PostSaveVo)

    // 수정
    fun updatePost(postUpdateVo: PostUpdateVo)

    // 삭제
    fun deletePostById(id: Long)

    // 검색
    fun searchPosts(keyword: String, page: Int, sortingRequest: SortingRequest): Page<PostSearchViewVo>
}
