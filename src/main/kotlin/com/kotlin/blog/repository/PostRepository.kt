package com.kotlin.blog.repository

import com.kotlin.blog.domain.entity.Post
import com.kotlin.blog.domain.vo.PostListViewVo
import com.kotlin.blog.domain.vo.PostViewVo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    @Query("SELECT NEW com.kotlin.blog.domain.vo.PostListViewVo(p.id, p.title, p.author.nickname, p.createdAt, p.updatedAt) FROM Post p")
    fun findAllPosts(pageable: Pageable): Page<PostListViewVo>

    @Query("SELECT NEW com.kotlin.blog.domain.vo.PostViewVo(p.id, p.title, p.content, p.author.nickname, p.createdAt, p.updatedAt) FROM Post p WHERE p.id = :id")
    fun findPostById(@Param("id") id: Long): PostViewVo

    @Modifying
    @Transactional
    @Query("INSERT INTO Post(title, content, createdAt, author) SELECT :title, :content, :createdAt, u FROM User u WHERE u.id = :userId")
    fun savePost(
        @Param("title") title: String,
        @Param("content") content: String,
        @Param("createdAt") createdAt: LocalDateTime,
        @Param("userId") userId: Long,
    )

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.title = :title, p.content = :content, p.updatedAt = :updatedAt WHERE p.id = :id")
    fun updatePost(
        @Param("id") id: Long,
        @Param("title") title: String,
        @Param("content") content: String,
        @Param("updatedAt") updatedAt: LocalDateTime,
    )
}
