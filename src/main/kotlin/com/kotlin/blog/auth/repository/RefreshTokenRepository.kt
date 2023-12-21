package com.kotlin.blog.auth.repository

import com.kotlin.blog.auth.domain.RefreshToken
import com.kotlin.blog.auth.domain.RefreshTokenVo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    @Query(
        "SELECT new com.kotlin.blog.auth.domain.RefreshTokenVo(rt.userId, rt.refreshToken, rt.id) " +
            "From RefreshToken rt " +
            "Where rt.userId = :userId",
    )
    fun findByUserIdOrNull(@Param("userId") userId: Long): RefreshTokenVo?

    @Modifying
    @Transactional
    @Query("INSERT INTO RefreshToken(userId, refreshToken) SELECT :userId, :refreshToken")
    fun saveRefreshToken(
        @Param("userId") userId: Long,
        @Param("refreshToken") refreshToken: String,
    )

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.refreshToken = :newRefreshToken WHERE rt.userId = :userId")
    fun updateRefreshToken(
        @Param("userId") userId: Long,
        @Param("newRefreshToken") newRefreshToken: String)
}
