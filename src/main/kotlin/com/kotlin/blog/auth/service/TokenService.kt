package com.kotlin.blog.auth.service

import com.kotlin.blog.auth.configuration.JwtProperties
import com.kotlin.blog.auth.controller.dto.response.AuthenticationResponse
import com.kotlin.blog.auth.jwt.JwtTokenUtil
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val jwtProperties: JwtProperties,
) {
    fun createToken(userDetails: UserDetails): AuthenticationResponse {
        val accessToken = createAccessToken(userDetails)
        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = "refreshToken",
        )
    }


//    fun createNewAccessToken(refreshToken: String): CreateAccessTokenResponse {
//
//        // 리프레시 토큰을 받으면 유효성 검증을 해야 하고 검증되지 않을 시 예외 발생
//        if(!jwtTokenUtil.isValid(refreshToken, )) {
//
//        }
//
//        createAccessToken()
//    }

    private fun createAccessToken(user: UserDetails): String {
        return jwtTokenUtil.generate(
            userDetails = user,
            expirationDate = getAccessTokenExpiration(),
        )
    }

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
}
