package com.kotlin.blog.auth.service

import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmailPasswordUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val applicationUser = userRepository.findByEmail(email)
        val userId = applicationUser?.id // jwt 토큰 발생 시 subject에 id를 넣고자 함

        return userId?.let {
            userRepository.findByEmail(email)
                ?.mapToUserDetails(it)
        }
            ?: throw UsernameNotFoundException("없는 사용자 Id입니다")
    }

    private fun ApplicationUser.mapToUserDetails(userId: Long): UserDetails =
        User.builder()
            .username(userId.toString())
            .password(this.password)
            .roles(this.role.name)
            .build()
}
