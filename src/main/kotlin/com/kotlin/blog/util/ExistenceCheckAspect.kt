package com.kotlin.blog.util

import com.kotlin.blog.repository.PostRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class ExistenceCheckAspect(
    private val postRepository: PostRepository
) {

    @Around("@annotation(ExistenceCheck) && args(id, ..)")
    fun existenceCheck(
        joinPoint: ProceedingJoinPoint,
        id: Long
    ): Any? {

        require (postRepository.existsById(id)) {
            "Post with Id $id does not exist"
        }

        return joinPoint.proceed()
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExistenceCheck


