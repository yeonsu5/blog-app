package com.kotlin.blog.auth.controller.dto.response

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
)
