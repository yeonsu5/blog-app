package com.kotlin.blog.auth.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val token: String,
    private val subject: String,
    private val role: MutableCollection<out GrantedAuthority>?,
) : AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return subject
    }
}
