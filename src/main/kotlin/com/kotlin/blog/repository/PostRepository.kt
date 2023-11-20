package com.kotlin.blog.repository

import com.kotlin.blog.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    override fun findAll(pageable: Pageable): Page<Post>
}
