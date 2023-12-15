package com.kotlin.blog.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class AuthenticationEntryPoint(
    // 엔트리 포인트는 스프링 시큐리티에서 인증과 관련된 예외가 발생했을 때 이를 처리하는 로직을 담당한다.
    @Qualifier("handlerExceptionResolver") // HandlerExceptionResolver의 빈이 두 종류가 있기 때문에 명시하기
    private val resolver: HandlerExceptionResolver, // HandlerExceptionResolver는 서블릿 컨테이너의 일부
) : AuthenticationEntryPoint {
    override fun commence(
        // commence()에서 스프링 시큐리티의 인증 관련 예외를 처리하게 된다.
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?, // 전달받은 AuthenticationException 예외
    ) {
        if (request != null && response != null && authException != null) {
            // Filter에서 request에 담아서 보내준 예외를 HandlerExceptionResolver가 처리
            // -> @RestControllerAdvice에 정의된 예외 처리 로직 활용

            if (request.getAttribute("exception") == null) { // custom filter 이외의 필터에서 예외 발생 시
                request.setAttribute("exception", authException) // authException 예외를 attribute에 추가
            }

            resolver.resolveException(request, response, null, request.getAttribute("exception") as Exception)
        }
    }
}
