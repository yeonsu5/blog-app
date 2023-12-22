package com.kotlin.blog.auth.service

import com.kotlin.blog.auth.configuration.JwtProperties
import com.kotlin.blog.auth.controller.dto.response.AuthenticationResponse
import com.kotlin.blog.auth.controller.dto.response.CreateAccessTokenResponse
import com.kotlin.blog.auth.jwt.JwtTokenUtil
import com.kotlin.blog.auth.repository.RefreshTokenRepository
import com.kotlin.blog.common.exception.InvalidTokenException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TokenService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userDetailsService: UserDetailsService,
) {
    @Transactional
    fun createToken(userDetails: UserDetails): AuthenticationResponse {
        val accessToken = createToken(userDetails, getAccessTokenExpiration())
        val refreshToken = createToken(userDetails, getRefreshTokenExpiration())

        // 리프레시 토큰이 이미 있으면 토큰을 갱신하고 없으면 새로 저장
        saveOrUpdateRefreshToken(userDetails.username.toLong(), refreshToken)

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun saveOrUpdateRefreshToken(userId: Long, newRefreshToken: String) {
        val existingTokenVo = refreshTokenRepository.findByUserIdOrNull(userId)

        if (existingTokenVo != null) {
            // 기존 토큰이 있는 경우 업데이트
            refreshTokenRepository.updateRefreshToken(userId, newRefreshToken)
        } else {
            // 기존 토큰이 없는 경우 새로 저장
            refreshTokenRepository.saveRefreshToken(userId, newRefreshToken)
        }
    }

    // 리프레시 토큰을 받으면 유효성 검증을 하고, 유효성 검증 통과 못할 시 예외 발생
    @Transactional
    fun createNewAccessToken(refreshToken: String): CreateAccessTokenResponse {
        val userId = jwtTokenUtil.extractUserId(refreshToken) // 리프레시 토큰에서 user Id 추출 // @@ 오류 발생
            ?: throw InvalidTokenException("유효하지 않은 리프레시 토큰입니다. 사용자 ID를 찾을 수 없습니다.")

        val userDetails = userDetailsService.loadUserByUsername(userId) // user의 userDetails 로드
        val storedRefreshToken =
            refreshTokenRepository.findByUserIdOrNull(userId.toLong())?.refreshToken // 데이터베이스에서 저장된 리프레시 토큰 조회
                ?: throw InvalidTokenException("저장된 리프레시 토큰을 찾을 수 없습니다.")

        if (!jwtTokenUtil.isRefreshTokenValid(refreshToken, storedRefreshToken)) { // 리프레시 토큰의 유효성 검증
            throw InvalidTokenException("유효하지 않은 리프레시 토큰입니다.")
        }

        val accessToken = createToken(userDetails, getAccessTokenExpiration()) // 새로운 액세스 토큰 생성 및 반환
        return CreateAccessTokenResponse(accessToken = accessToken)
    }

    private fun createToken(user: UserDetails, expiration: Date): String {
        return jwtTokenUtil.generate(
            userDetails = user,
            expirationDate = expiration,
        )
    }

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

    private fun getRefreshTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
}
