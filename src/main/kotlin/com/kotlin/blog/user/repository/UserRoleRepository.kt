package com.kotlin.blog.user.repository

import com.kotlin.blog.common.authority.Role
import com.kotlin.blog.user.domain.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface UserRoleRepository : JpaRepository<UserRole, Long> {

    @Modifying
    @Transactional
    @Query("INSERT INTO UserRole(role, user) SELECT :role, u FROM User u WHERE u.id = :userId")
    fun saveRole(
        @Param("role") role: Role,
        @Param("userId") userId: Long,
    )
}
