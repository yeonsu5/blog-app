package com.kotlin.blog.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.Exception

@Component
class JwtAuthenticationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
) : OncePerRequestFilter() { // Http 요청마다 한 번씩 실행

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader: String = request.getHeader("Authorization") ?: ""
        try {
            if (authHeader.containsBearerToken()) { // authHeader가 Bearer 로 시작하는 경우
                val jwtToken = authHeader.extractTokenValue()
                val userId = jwtTokenUtil.extractUserId(jwtToken) ?: "" // 토큰에서 이메일 값 추출
                val authority = jwtTokenUtil.extractAuthority(jwtToken)

                val jwtAuthenticationToken = JwtAuthenticationToken(
                    jwtToken,
                    userId,
                    authority,
                ) // AbstractAuthenticationToken을 상속한 JwtAuthenticationToken 객체
                val authenticatedJwtToken =
                    jwtAuthenticationProvider.authenticate(jwtAuthenticationToken) // Authentication 객체

                if (authenticatedJwtToken.isAuthenticated) {
                    SecurityContextHolder.getContext().authentication =
                        authenticatedJwtToken // Authentication 객체를 SecurityContext에 추가
                }
            }
        } catch (ex: Exception) {
            request.setAttribute("exception", ex) // try-catch로 예외를 감지하여 해당 예외를 request에 추가
        }
        filterChain.doFilter(request, response)
    }

    private fun String.containsBearerToken() =
        this != "" || this.startsWith("Bearer ")

    private fun String.extractTokenValue() =
        this.substringAfter("Bearer ")
}
