package com.kotlin.blog.auth.service

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
class JwtUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByIdOrNull(username.toLong())
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("해당 번호의 사용자를 찾을 수 없습니다")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.id.toString())
            .password(this.password)
            .roles(this.role.name)
            .build()
}
