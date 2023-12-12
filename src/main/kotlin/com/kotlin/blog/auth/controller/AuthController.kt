package com.kotlin.blog.auth.controller

import com.kotlin.blog.auth.controller.dto.AuthenticationResponse
import com.kotlin.blog.auth.service.AuthenticationService
import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
) {

    @PostMapping("/login")
    fun login(): ResponseEntity<ApiResponse<AuthenticationResponse>> {
        // 이미 필터를 통해 인증된 사용자의 정보를 SecurityContextHolder에서 가져오기
        val authentication = SecurityContextHolder.getContext().authentication

        val userDetails = authentication.principal as UserDetails

        val authenticationResponse = authenticationService.createToken(userDetails)

        return createResponse(HttpStatus.OK, data = authenticationResponse)
    }
}
