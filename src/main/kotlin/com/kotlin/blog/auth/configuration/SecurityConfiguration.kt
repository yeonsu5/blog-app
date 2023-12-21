package com.kotlin.blog.auth.configuration

import com.kotlin.blog.auth.emailPassword.EmailPasswordAuthenticationFilter
import com.kotlin.blog.auth.jwt.JwtAuthenticationFilter
import com.kotlin.blog.auth.jwt.JwtAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val emailPasswordAuthenticationProvider: AuthenticationProvider,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val entryPoint: AuthenticationEntryPoint,
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        emailPasswordAuthenticationFilter: EmailPasswordAuthenticationFilter,
    ): DefaultSecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/users/register", "/error")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/posts/**") // 글 조회(전체, 상세, 검색)는 모두에게 허용
                    .permitAll()
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(emailPasswordAuthenticationProvider)
            .authenticationProvider(jwtAuthenticationProvider)
            .addFilterBefore(emailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }

        return http.build()
    }
}
