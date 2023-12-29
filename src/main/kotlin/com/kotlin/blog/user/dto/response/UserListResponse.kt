package com.kotlin.blog.user.dto.response

import com.kotlin.blog.user.domain.vo.UserListVo
import java.time.LocalDateTime

data class UserListResponse(
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val id: Long,
) {
    companion object {
        fun voToDto(userListVo: UserListVo): UserListResponse {
            return UserListResponse(
                email = userListVo.email,
                nickname = userListVo.nickname,
                createdAt = userListVo.createdAt,
                updatedAt = userListVo.updatedAt,
                id = userListVo.id,
            )
        }
    }
}
