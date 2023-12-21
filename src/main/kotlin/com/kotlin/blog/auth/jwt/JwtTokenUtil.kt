package com.kotlin.blog.auth.jwt

import com.kotlin.blog.auth.configuration.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenUtil(
    jwtProperties: JwtProperties,
) {

    private val secretKey = Keys.hmacShaKeyFor(
        // 주어진 바이트 배열을 기반으로 HMAC-SHA 알고리즘에 사용될 비밀키를 생성. 비밀키는 서명 생성과 검증에 사용됨.
        jwtProperties.key.toByteArray(), // jwtProperties의 key 프로퍼티 값을 바이트 배열로 변환
    )

    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
    ): String {
        val role = userDetails.authorities.first().authority

        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add("role", role)
            .and()
            .signWith(secretKey)
            .compact()
    }

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val userId = extractUserId(token)

        return userDetails.username == userId && !isExpired(token)
    }

    fun isRefreshTokenValid(receivedToken: String, storedToken: String): Boolean {
        return receivedToken == storedToken && !isExpired(receivedToken)
    }

    fun extractUserId(token: String): String? =
        getAllClaims(token)
            .subject

    fun extractAuthority(token: String): MutableCollection<GrantedAuthority>? {
        val role = getAllClaims(token)["role"] as String
        return mutableListOf(SimpleGrantedAuthority(role))
    }

    fun isExpired(token: String): Boolean = // 만료 시간이 현재 시간 이전이면 만료된 토큰
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}
