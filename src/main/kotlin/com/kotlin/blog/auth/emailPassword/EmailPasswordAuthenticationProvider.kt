package com.kotlin.blog.auth.emailPassword

import com.kotlin.blog.auth.service.EmailPasswordUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

// DaoAuthenticationProvider를 래핑하여 사용하기 위한 클래스
@Component
class EmailPasswordAuthenticationProvider(
    userDetailsService: EmailPasswordUserDetailsService,
    passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {

    private val daoAuthenticationProvider: DaoAuthenticationProvider = DaoAuthenticationProvider()
    // DaoAuthenticationProvider의 인스턴스를 내부 필드로 가지고 메서드를 호출할 때 사용

    init {
        daoAuthenticationProvider.setUserDetailsService(userDetailsService) // 로그인 인증에 사용할 커스텀 UserDetailsService
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        return daoAuthenticationProvider.authenticate(authentication)
    }

    override fun supports(authenticationClass: Class<*>): Boolean {
        return daoAuthenticationProvider.supports(authenticationClass)
    }
}
