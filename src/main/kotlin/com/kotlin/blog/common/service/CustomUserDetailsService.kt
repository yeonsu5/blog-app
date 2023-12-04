package com.kotlin.blog.common.service

import com.kotlin.blog.user.domain.entity.User
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails =
        userRepository.findByEmail(email)
            ?.let { createUserDetails(it) }
            ?: throw UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.")

    private fun createUserDetails(user: User): UserDetails =
        org.springframework.security.core.userdetails.User(
            user.email,
//            passwordEncoder.encode(user.password), //
            user.password, // 이미 저장된 인코딩된 비밀번호를 사용
            user.userRole.map { SimpleGrantedAuthority("ROLE_${it.role}") },
        )
}
