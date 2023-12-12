package com.kotlin.blog.auth.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExpiration: Long,
)
// application.yml에서 "jwt" 접두사로 시작하는 프로퍼티들을 가져오기
