package com.kotlin.blog.auth.controller

import com.kotlin.blog.auth.controller.dto.response.AuthenticationResponse
import com.kotlin.blog.auth.controller.dto.response.CreateAccessTokenResponse
import com.kotlin.blog.auth.service.TokenService
import com.kotlin.blog.common.exception.LoginFailureException
import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val tokenService: TokenService,
) {

    @PostMapping("/login")
    fun login(): ResponseEntity<ApiResponse<AuthenticationResponse>> {
        // 이미 필터를 통해 인증된 사용자의 정보를 SecurityContextHolder에서 가져오기
        val authentication = SecurityContextHolder.getContext().authentication

        // 로그인 실패 시 SecurityContextHolder에 AnonymousAuthenticationToken 생성
        if (authentication is AnonymousAuthenticationToken) {
            throw LoginFailureException()
        }
        val userDetails = authentication.principal as UserDetails

        val authenticationResponse = tokenService.createAccessTokenAndRefreshToken(userDetails)

        return createResponse(HttpStatus.OK, data = authenticationResponse)
    }

    @PostMapping("/refresh")
    fun createNewAccessToken(
        @RequestHeader("Authorization") refreshToken: String, // 클라이언트는 헤더에 refreshToken을 보냄
    ): ResponseEntity<ApiResponse<CreateAccessTokenResponse>> {
        val newAccessToken = tokenService.createNewAccessToken(refreshToken)

        return createResponse(HttpStatus.CREATED, data = newAccessToken)
    }

    @GetMapping("/google")
    fun redirectToGoogleLogin(): RedirectView {
        return RedirectView("/oauth2/authorization/google")
        // 이 경로로 요청을 보내면 OAuth2AuthorizationRequestRedirectFilter가 사용자를 Google 인증 페이지로 리다이렉트한다.
    }
}
