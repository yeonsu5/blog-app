package com.kotlin.blog.common.security.service

import com.kotlin.blog.common.security.configuration.JwtProperties
import com.kotlin.blog.common.security.controller.dto.AuthenticationResponse
import com.kotlin.blog.common.security.controller.dto.UserLoginRequest
import com.kotlin.blog.common.security.jwt.TokenUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenUtil: TokenUtil,
    private val jwtProperties: JwtProperties,
) {
    fun authentication(authenticationRequest: UserLoginRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password,
            ),
        )

        val user = userDetailsService.loadUserByUsername(authenticationRequest.email)

        val accessToken = createAccessToken(user)

        return AuthenticationResponse(
            accessToken = accessToken,
        )
    }

    private fun createAccessToken(user: UserDetails) = tokenUtil.generate(
        userDetails = user,
        expirationDate = getAccessTokenExpiration(),
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
}
