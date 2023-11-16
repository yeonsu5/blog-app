package com.kotlin.blog.dto.response

import com.kotlin.blog.domain.Post
import java.time.LocalDateTime

data class PostResponse(
    val title: String,
    val content: String,
    val author: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val id: Long,
) {
    companion object {
        fun toDto(post: Post): PostResponse {
            return PostResponse(
                title = post.title,
                content = post.content,
                author = post.author.nickname,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
                id = post.id,
            )
        }
    }
    /*
    * companion object는 클래스 내부에 선언되며, 그 클래스의 모든 인스턴스가 공유하는 객체를 생성하는 데 사용된다.
    * 이는 자바의 정적(static) 메소드나 필드와 유사하게 동작한다.
    * 클래스 내부에서 한 번만 선언될 수 있으며, 해당 클래스의 이름을 사용하여 직접 접근할 수 있다.
    * 이를 통해 클래스 레벨에서 메소드나 속성을 사용할 수 있다.
    * */
}
