package com.kotlin.blog.auth.configuration

import com.kotlin.blog.auth.service.EmailPasswordUserDetailsService
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class) // JwtProperties의 프로퍼티를 활성화
class SecurityBeansConfiguration {

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun emailPasswordAuthenticationProvider(userRepository: UserRepository): AuthenticationProvider =
        DaoAuthenticationProvider()
            .also { // also: 수신 객체를 그대로 반환하면서 람다 블록 내에서 추가적인 작업 수행
                it.setUserDetailsService(emailPasswordUserDetailsService(userRepository))
                it.setPasswordEncoder(encoder())
            }
    // 로그인 인증 과정에서 사용될 AuthenticationProvider 빈
    // DaoAuthenticationProvider는 데이터베이스에서 사용자 정보를 가져오고, 비밀번호를 확인해 인증을 처리한다.
    // CustomUserDetailsService에서 유저 정보를 데이터베이스에서 가져온다.

    fun emailPasswordUserDetailsService(userRepository: UserRepository): UserDetailsService =
        EmailPasswordUserDetailsService(userRepository)
}
