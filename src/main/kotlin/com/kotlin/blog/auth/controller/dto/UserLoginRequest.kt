package com.kotlin.blog.auth.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UserLoginRequest @JsonCreator constructor(

    @JsonProperty("email")
    @field:NotBlank
    val email: String,

    @JsonProperty("password")
    @field:NotBlank
    val password: String,
)
