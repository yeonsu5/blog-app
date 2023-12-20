package com.kotlin.blog.auth.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val token: String,
    private val subject: String,
    role: MutableCollection<out GrantedAuthority>?,
) : AbstractAuthenticationToken(role) {
    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return subject
    }
}
