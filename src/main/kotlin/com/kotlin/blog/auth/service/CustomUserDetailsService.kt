package com.kotlin.blog.auth.service

import com.kotlin.blog.user.repository.RoleRepository
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = com.kotlin.blog.user.domain.entity.User
// 엔티티 User 클래스에 대한 별칭 지정 -> userdetails의 User와 구별하기 위해 사용

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // username이 이메일 형식인지(로그인), 아니면 숫자 형식인지(인가) 확인
        val applicationUser = if (username.contains("@")) {
            // 이메일로 사용자 찾기
            userRepository.findByEmail(username)
        } else {
            // ID로 사용자 찾기(jwt토큰-인가)
            userRepository.findByIdOrNull(username.toLong())
        }

        val userId = applicationUser?.id

        return userId?.let {
            applicationUser.mapToUserDetails(it)
        } ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")
    }

    private fun ApplicationUser.mapToUserDetails(userId: Long): UserDetails {
        val userRoles = roleRepository.findByUserId(userId)
            .map { it.name } // Role 엔티티에서 이름만 추출
            .toTypedArray() // Array로 변환

        return User.builder()
            .username(userId.toString())
            .password(this.password ?: "") // social login 회원가입인 경우 비밀번호가 빈값
            .roles(*userRoles) // 스프레드 연산자(*)를 사용하여 모든 역할 추가
            .build()
    }
}
