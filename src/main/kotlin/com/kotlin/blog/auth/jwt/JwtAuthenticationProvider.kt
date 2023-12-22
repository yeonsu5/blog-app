package com.kotlin.blog.auth.jwt

import com.kotlin.blog.common.exception.InvalidTokenException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userDetailsService: UserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials as String

        val username = jwtTokenUtil.extractUserId(token)
        val userDetails = username?.let { userDetailsService.loadUserByUsername(it) }
            ?: throw InvalidTokenException("유효하지 않은 토큰: 사용자를 찾을 수 없습니다")

        if (!jwtTokenUtil.isValid(token, userDetails)) {
            throw InvalidTokenException("유효하지 않은 토큰: 토큰 검증 실패")
        }

        val jwtAuthenticationToken = JwtAuthenticationToken(token, userDetails.username, userDetails.authorities)
        jwtAuthenticationToken.isAuthenticated = true

        return jwtAuthenticationToken
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
