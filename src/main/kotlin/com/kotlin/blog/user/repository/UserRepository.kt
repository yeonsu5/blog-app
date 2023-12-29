package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickName: String): Boolean

    fun findByEmail(email: String): User?
}
