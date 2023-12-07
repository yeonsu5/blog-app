package com.kotlin.blog.common.security.jwt

import com.kotlin.blog.common.security.service.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenUtil: TokenUtil,
) : OncePerRequestFilter() { // Http 요청마다 한 번씩 실행

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader.doesNotContainBearerToken()) { // authHeader가 null이거나 Bearer 로 시작하지 않는 경우
            filterChain.doFilter(request, response) // 다음 필터로 요청을 전달
            return
        }

        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenUtil.extractEmail(jwtToken) // 토큰에서 이메일 값 추출

        if (email != null && SecurityContextHolder.getContext().authentication == null) { // 현재 컨텍스트의 인증객체가 null인지 확인
            val foundUser = userDetailsService.loadUserByUsername(email) // foundUser는 UserDetails 객체

            if (tokenUtil.isValid(jwtToken, foundUser)) { // 토큰의 이메일과 DB에서 가져온 이메일이 일치한다면 & 토큰이 만료되지 않았다면
                updateContext(foundUser, request) // Security Context를 업데이트

                filterChain.doFilter(request, response) // 다음 필터로 요청을 전달
            }
        }
    }

    private fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue() =
        this.substringAfter("Bearer ")

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) { // foundUser와 현재 요청을 기반으로
        val authToken = UsernamePasswordAuthenticationToken(
            foundUser,
            null,
            foundUser.authorities,
        ) // UsernamePasswordAuthenticationToken을 생성하고
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request) // 인증 요청의 세부 정보를 token의 detail에 추가하기
        SecurityContextHolder.getContext().authentication = authToken // 컨텍스트의 인증 객체를 업데이트
    }
}
