package com.kotlin.blog.common.security.configuration

import com.kotlin.blog.common.security.service.CustomUserDetailsService
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class) // JwtProperties의 프로퍼티를 활성화
class Configuration {

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService =
        CustomUserDetailsService(userRepository)

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(userRepository: UserRepository): AuthenticationProvider =
        DaoAuthenticationProvider()
            .also { // also: 수신 객체를 그대로 반환하면서 람다 블록 내에서 추가적인 작업 수행
                it.setUserDetailsService(userDetailsService(userRepository))
                it.setPasswordEncoder(encoder())
            }
    // 인증 과정에서 사용될 AuthenticationProvider 빈
    // DaoAuthenticationProvider는 데이터베이스에서 사용자 정보를 가져오고, 비밀번호를 확인해 인증을 처리한다.
    // CustomUserDetailsService에서 유저 정보를 데이터베이스에서 가져온다.

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
    // AuthenticationConfiguration를 매개변수로 받아 authenticationManager를 반환하는 빈(매니저를 제공)
}
