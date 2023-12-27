package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.Role
import com.kotlin.blog.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickName: String): Boolean

    fun findByEmail(email: String): User?

    @Modifying
    @Transactional
    @Query("INSERT INTO User(email, password, createdAt, nickname, role) SELECT :email, :password, :createdAt, :nickname, :role")
    fun register(
        @Param("email") email: String,
        @Param("password") password: String?,
        @Param("createdAt") createdAt: LocalDateTime,
        @Param("nickname") nickname: String,
        @Param("role") role: Role,
    )
}
