package com.kotlin.blog.service

import com.kotlin.blog.domain.vo.PostListViewVo
import com.kotlin.blog.domain.vo.PostSaveVo
import com.kotlin.blog.domain.vo.PostSearchViewVo
import com.kotlin.blog.domain.vo.PostUpdateVo
import com.kotlin.blog.domain.vo.PostViewVo
import com.kotlin.blog.dto.request.OrderBy
import com.kotlin.blog.dto.request.SortingRequest
import com.kotlin.blog.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
) : PostService {

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun getAllPosts(page: Int, sortingRequest: SortingRequest): Page<PostListViewVo> {
        val pageable = creatingPageable(page, sortingRequest)

        return postRepository.findAllPosts(pageable)
    }

    override fun getPostById(id: Long): PostViewVo { // get 요청일 때는 @Transactional 적용하지 않기
        return postRepository.findPostById(id)
    }

    @Transactional
    override fun savePost(postSaveVo: PostSaveVo) {
        postRepository.savePost(postSaveVo.title, postSaveVo.content, postSaveVo.createdAt, postSaveVo.userId)
    }

    @Transactional
    override fun deletePostById(id: Long) {
        postRepository.deleteById(id)
    }

    @Transactional
    override fun updatePost(postUpdateVo: PostUpdateVo) {
        postRepository.updatePost(postUpdateVo.id, postUpdateVo.title, postUpdateVo.content, postUpdateVo.updatedAt)
    }

    override fun searchPosts(keyword: String, page: Int, sortingRequest: SortingRequest): Page<PostSearchViewVo> {
        val pageable = creatingPageable(page, sortingRequest)

        return postRepository.searchPosts(keyword, pageable)
    }

    private fun creatingPageable(page: Int, sortingRequest: SortingRequest): Pageable {
        val sortDirection = if (sortingRequest.orderBy == OrderBy.ASC) Sort.Direction.ASC else Sort.Direction.DESC
        val sortingField = sortingRequest.sortBy.fieldName

        return PageRequest.of(page, PAGE_SIZE, Sort.by(sortDirection, sortingField))
    }
}
