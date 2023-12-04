package com.kotlin.blog.user.service

import com.kotlin.blog.common.authority.TokenInfo
import com.kotlin.blog.user.domain.vo.UserLoginVo
import com.kotlin.blog.user.domain.vo.UserRegisterVo

interface UserService {
    fun register(userRegisterVo: UserRegisterVo)

    fun login(userLoginVo: UserLoginVo): TokenInfo
}
