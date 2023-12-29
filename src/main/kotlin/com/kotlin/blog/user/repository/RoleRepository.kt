package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByUserId(userId: Long): List<Role>
}
