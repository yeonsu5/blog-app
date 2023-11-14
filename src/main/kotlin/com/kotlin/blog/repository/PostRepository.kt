package com.kotlin.blog.repository

import com.kotlin.blog.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
