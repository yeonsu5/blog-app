package com.kotlin.blog.auth.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.blog.auth.service.TokenService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
//    private val tokenUtil: JwtTokenUtil,
    private val tokenService: TokenService,
    private val userDetailsService: UserDetailsService,
    private val objectMapper: ObjectMapper,
) : SimpleUrlAuthenticationSuccessHandler() {
    // 스프링 시큐리티의 기본 로직에서는 별도의 authenticationSuccessHandler를 지정하지 않으면 로그인 성공 이후
    // SimpleUrlAuthenticationSuccessHandler를 사용한다.
    // 일반적인 로직은 동일하게 사용하고, 토큰과 관련된 작업만 추가로 처리하기 위해
    // SimpleUrlAuthenticationHandler를 상속받은 뒤에 onAuthenticationSuccess() 메서드만 오버라이드 한다.

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        // JWT 토큰 생성 및 처리 로직
        val oAuth2User = authentication.principal as OAuth2User
        val email = oAuth2User.attributes["email"] as String

        // CustomUserDetailsService를 사용하여 UserDetails 로드
        val userDetails = userDetailsService.loadUserByUsername(email)

        // UserDetails를 사용하여 토큰 생성
        val authenticationResponse = tokenService.createAccessTokenAndRefreshToken(userDetails)

        // ObjectMapper를 사용하여 Json 문자열 생성
        val jsonResponse = objectMapper.writeValueAsString(authenticationResponse)

        // JSON 응답 전송
        response.contentType = "application/json;charset=UTF-8"
        response.writer.write(jsonResponse)

        // 세션에서 인증 관련 속성 제거
        clearAuthenticationAttributes(request)
    }
}
