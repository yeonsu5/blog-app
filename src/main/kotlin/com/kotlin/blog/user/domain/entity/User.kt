package com.kotlin.blog.user.domain.entity

import com.kotlin.blog.post.domain.entity.Post
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Column(unique = true)
    val email: String,

    val password: String,

    @Column(unique = true)
    var nickname: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) {
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var posts: MutableList<Post> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val userRole: List<UserRole> = listOf()
}
