package com.kotlin.blog.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Post(
    var title: String,
    var content: String,

    @ManyToOne
    var author: User,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    fun update(title: String, content: String, updatedAt: LocalDateTime) {
        this.title = title
        this.content = content
        this.updatedAt = updatedAt
    }
}
