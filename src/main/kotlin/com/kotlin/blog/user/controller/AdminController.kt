package com.kotlin.blog.user.controller

import com.kotlin.blog.common.util.ApiResponse
import com.kotlin.blog.common.util.createResponse
import com.kotlin.blog.user.dto.response.UserListResponse
import com.kotlin.blog.user.service.AdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService,
) {
    @GetMapping("/users")
    private fun findAll(): ResponseEntity<ApiResponse<List<UserListResponse>>> {
        val users = adminService.findAllUsers().map { Uservo ->
            UserListResponse.voToDto(Uservo)
        }
        return createResponse(HttpStatus.OK, data = users)
    }
}
