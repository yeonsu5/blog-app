package com.kotlin.blog.service

import com.kotlin.blog.domain.vo.PostListViewVo
import com.kotlin.blog.domain.vo.PostSaveVo
import com.kotlin.blog.domain.vo.PostUpdateVo
import com.kotlin.blog.domain.vo.PostViewVo
import com.kotlin.blog.dto.request.SortingRequest
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
}
