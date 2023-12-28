package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.SocialLogin
import org.springframework.data.jpa.repository.JpaRepository

interface SocialLoginRepository : JpaRepository<SocialLogin, Long>
