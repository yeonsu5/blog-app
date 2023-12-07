package com.kotlin.blog.user.service

import com.kotlin.blog.user.domain.vo.UserRegisterVo

interface UserService {
    fun register(userRegisterVo: UserRegisterVo)
}
