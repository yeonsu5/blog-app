package com.kotlin.blog.auth

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import java.util.function.Supplier

@Component
class TestAuthorizationManager : AuthorizationManager<RequestAuthorizationContext> {

    private val antPathMatcher = AntPathMatcher()

    override fun check(
        authentication: Supplier<Authentication>,
        context: RequestAuthorizationContext,
    ): AuthorizationDecision {
        val a: Authentication = authentication.get()
        // UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_ADMIN, ROLE_USER]], Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_ADMIN, ROLE_USER]]
        // JwtAuthenticationToken [Principal=2, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USER]]
        // AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]
        val ad: AuthorizationDecision = AuthorizationDecision(false)
        // AuthorizationDecision [granted=false]
//        println(a)
//        println(a.authorities) // [ROLE_ANONYMOUS] // [ROLE_ADMIN, ROLE_USER]
//        println(a.principal) // anonymousUser // 1 (userId인듯)
//        println(a.principal.javaClass) // class java.lang.String
//        println(ad)
//        println(context.request) // SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@35aaff72]
//        println(context.request.method) // 현재 요청에서 method 가져오기(예: GET)
//        println(context.request.requestURI) // 현재 요청에서 url 가져오기(예: /api/posts/search)
//        println(context.variables) // {} // 뭔지 모르겠음.

        // db에서 가져왔다 치고 작성함
        val permissionMethod = "POST"
        val permissionUri = "/api/posts"

        var hasPermission = false
        if (permissionMethod == context.request.method && antPathMatcher.match(permissionUri, context.request.requestURI)) {
            hasPermission = true
        }


        return AuthorizationDecision(hasPermission)
    }
}
