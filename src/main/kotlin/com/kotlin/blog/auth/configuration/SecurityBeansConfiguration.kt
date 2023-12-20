package com.kotlin.blog.auth.configuration

import com.kotlin.blog.auth.emailPassword.EmailPasswordAuthenticationProvider
import com.kotlin.blog.auth.jwt.JwtAuthenticationProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class) // JwtProperties의 프로퍼티를 활성화
class SecurityBeansConfiguration {

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(
        jwtAuthenticationProvider: JwtAuthenticationProvider,
        emailPasswordAuthenticationProvider: EmailPasswordAuthenticationProvider,
        // manager가 관리하는 provider 추가
    ): AuthenticationManager {
        return ProviderManager(jwtAuthenticationProvider, emailPasswordAuthenticationProvider)
    }
}
