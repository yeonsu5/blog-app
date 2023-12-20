package com.kotlin.blog.auth.emailPassword

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kotlin.blog.auth.controller.dto.request.UserLoginRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class EmailPasswordAuthenticationFilter(
//    private val emailPasswordAuthenticationProvider: AuthenticationProvider,
    private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {

    val objectMapper = jacksonObjectMapper() // Kotlin용 Jackson 모듈에서 제공하는 확장함수(ObjectMapper + KotlinModule)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            if (isLoginRequest(request)) {
                val authenticationRequest = objectMapper.readValue(request.inputStream, UserLoginRequest::class.java)
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(
                        authenticationRequest.email,
                        authenticationRequest.password,
                    )
                // 토큰 사용
                val authenticatedToken =
                    authenticationManager.authenticate(usernamePasswordAuthenticationToken)

                if (authenticatedToken.isAuthenticated) {
                    SecurityContextHolder.getContext().authentication =
                        authenticatedToken
                }
            }
        } catch (ex: Exception) {
            request.setAttribute("exception", ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun isLoginRequest(request: HttpServletRequest) =
        request.servletPath == "/api/auth/login" && request.method == "POST"
}
