package com.kotlin.blog.user.controller

import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.dto.request.UserRegisterRequest
import com.kotlin.blog.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun hello(): String = "hello"

    @PostMapping("/register")
    fun register(
        @RequestBody @Valid
        request: UserRegisterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userRegisterVo = UserRegisterVo(request.email, request.password, request.nickname)

        userService.register(userRegisterVo)

        return createResponse(HttpStatus.OK)
    }
}
