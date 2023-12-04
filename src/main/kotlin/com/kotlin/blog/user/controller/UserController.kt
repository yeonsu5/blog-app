package com.kotlin.blog.user.controller

import com.kotlin.blog.common.authority.TokenInfo
import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import com.kotlin.blog.user.domain.vo.UserLoginVo
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.dto.request.UserLoginRequest
import com.kotlin.blog.user.dto.request.UserRegisterRequest
import com.kotlin.blog.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/register")
    fun register(
        @RequestBody @Valid
        request: UserRegisterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userRegisterVo = UserRegisterVo(request.email, request.password, request.nickname)

        userService.register(userRegisterVo)

        return createResponse(HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid
        request: UserLoginRequest,
    ): ResponseEntity<ApiResponse<TokenInfo>> {
        val userLoginVo = UserLoginVo(request.email, request.password)

        val tokenInfo = userService.login(userLoginVo)

        return createResponse(HttpStatus.OK, data = tokenInfo)
    }
}
