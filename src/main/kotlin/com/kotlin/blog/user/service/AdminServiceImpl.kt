package com.kotlin.blog.user.service

import com.kotlin.blog.user.domain.vo.UserListVo
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository,
) : AdminService {
    override fun findAllUsers(): List<UserListVo> {
        return userRepository.findAll().map { user ->
            UserListVo(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
        }
    }
}
