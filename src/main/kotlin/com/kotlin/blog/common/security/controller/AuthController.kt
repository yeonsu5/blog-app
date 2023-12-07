package com.kotlin.blog.common.security.controller

import com.kotlin.blog.common.security.controller.dto.AuthenticationResponse
import com.kotlin.blog.common.security.controller.dto.UserLoginRequest
import com.kotlin.blog.common.security.service.AuthenticationService
import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
) {

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid
        request: UserLoginRequest,
    ): ResponseEntity<ApiResponse<AuthenticationResponse>> {
        val authenticationResponse = authenticationService.authentication(request)

        return createResponse(HttpStatus.OK, data = authenticationResponse)
    }
}
