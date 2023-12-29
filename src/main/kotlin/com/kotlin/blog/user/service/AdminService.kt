package com.kotlin.blog.user.service

import com.kotlin.blog.user.domain.vo.UserListVo

interface AdminService {
    fun findAllUsers(): List<UserListVo>
}
