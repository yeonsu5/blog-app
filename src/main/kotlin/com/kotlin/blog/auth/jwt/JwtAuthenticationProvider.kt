package com.kotlin.blog.auth.jwt

import com.kotlin.blog.auth.service.JwtUserDetailsService
import com.kotlin.blog.common.exception.InvalidTokenException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userDetailsService: JwtUserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials as String

        val username = jwtTokenUtil.extractUserId(token)
        val userDetails = username?.let { userDetailsService.loadUserByUsername(it) }
            ?: throw InvalidTokenException("Invalid token: Cannot find user")

        if (!jwtTokenUtil.isValid(token, userDetails)) {
            throw InvalidTokenException("Invalid token: Token validation failed")
        }

        val jwtAuthenticationToken = JwtAuthenticationToken(token, userDetails.username, userDetails.authorities)
        jwtAuthenticationToken.isAuthenticated = true

        return jwtAuthenticationToken
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
