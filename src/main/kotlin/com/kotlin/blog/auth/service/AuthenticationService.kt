package com.kotlin.blog.auth.service

import com.kotlin.blog.auth.configuration.JwtProperties
import com.kotlin.blog.auth.controller.dto.AuthenticationResponse
import com.kotlin.blog.auth.jwt.JwtTokenUtil
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val jwtProperties: JwtProperties,
) {
    fun createToken(userDetails: UserDetails): AuthenticationResponse {
        val accessToken = createAccessToken(userDetails)
        return AuthenticationResponse(
            accessToken = accessToken,
        )
    }

    private fun createAccessToken(user: UserDetails): String {
        return jwtTokenUtil.generate(
            userDetails = user,
            expirationDate = getAccessTokenExpiration(),
        )
    }

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
}
