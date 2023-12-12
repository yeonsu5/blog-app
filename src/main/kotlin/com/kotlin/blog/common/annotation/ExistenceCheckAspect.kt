package com.kotlin.blog.common.annotation

import com.kotlin.blog.post.repository.PostRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class ExistenceCheckAspect(
    private val postRepository: PostRepository
) {

    @Around("@annotation(com.kotlin.blog.common.annotation.ExistenceCheck) && args(id, ..)")
    fun existenceCheck(
        joinPoint: ProceedingJoinPoint,
        id: Long
    ): Any? {

        require (postRepository.existsById(id)) {
            "Id가 ${id}인 게시물이 존재하지 않습니다"
        }

        return joinPoint.proceed()
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExistenceCheck


