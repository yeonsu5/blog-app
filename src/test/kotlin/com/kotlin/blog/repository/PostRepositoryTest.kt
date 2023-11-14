package com.kotlin.blog.repository

import com.kotlin.blog.domain.Post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostRepositoryTest(
    @Autowired val postRepository: PostRepository,
) {

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("전체 글 조회 테스트(레포지토리)")
    fun findAllTest() {
        // given
        val post1 = Post("test title 1", "test content 1")
        val post2 = Post("test title 2", "test content 2")
        postRepository.save(post1)
        postRepository.save(post2)
        // when
        val results = postRepository.findAll()
        // then
        assertThat(results.size).isEqualTo(2)
        assertThat(results[0].title).isEqualTo(post1.title)
        assertThat(results[1].content).isEqualTo(post2.content)
    }

    @Test
    @DisplayName("글 1개 조회 테스트(레포지토리)")
    fun findByIdTest() {
        // given
        val post = Post("test title", "test content")
        postRepository.save(post)
        // when
        val result = postRepository.findByIdOrNull(post.id)
        // then
        assertThat(result).isNotNull
        assertThat(result?.title).isEqualTo(post.title)
        assertThat(result?.content).isEqualTo(post.content)
    }

    @Test
    @DisplayName("글 저장 테스트(레포지토리)")
    fun saveTest() {
        // given
        val post = Post("test title", "test content")
        // when
        val result = postRepository.save(post)
        // then
        assertThat(result).isEqualTo(post)
        assertThat(result).isInstanceOf(Post::class.java) // Post::class 라는 Kotlin의 클래스 레퍼런스를 java 클래스로 변환
    }

    @Test
    @DisplayName("글 수정 테스트(레포지토리)")
    fun updateTest() {
        // given
        val post = Post("initial title", "initial content")
        val savedPost = postRepository.save(post)
        val updatedTitle = "updated title"
        val updatedContent = "updated content"

        savedPost.title = updatedTitle
        savedPost.content = updatedContent

        // when
        val updatedPost = postRepository.save(savedPost)

        // then
        assertThat(updatedPost.id).isEqualTo(savedPost.id)
        assertThat(updatedPost.title).isEqualTo(updatedTitle)
        assertThat(updatedPost.content).isEqualTo(updatedContent)
    }

    @Test
    @DisplayName("글 삭제 테스트(레포지토리)")
    fun deleteTest() {
        // given
        val post = Post("to be deleted post title", "to be deleted post content")
        val savedPost = postRepository.save(post)
        // when
        postRepository.deleteById(savedPost.id!!)
        /*
        !! 연산자를 사용하여 savedPost.id가 null일 경우 NullPointerException을 던지도록 강제하였다.
         테스트 코드에서는 예상치 못한 상황이 발생했을 때 즉시 테스트를 실패시키는 게 좋으므로 테스트 코드에서 이렇게 작성하는건 괜찮지만
         이런 방식의 null 처리는 실제 프로덕션 코드에서는 권장되지 않는다. 프로덕션 코드에서는 가능한 한 안전한 null-safe 방식을 사용해야 한다.
         예를 들어
         savedPost.id?.let { postRepository.deleteById(it) }
         이러한 방식으로 작성하면 savedPost.id가 null인 경우 deleteById 메소드가 호출되지 않는다.
        */

        // then
        assertThat(postRepository.findById(savedPost.id!!)).isEmpty()
        assertThat(postRepository.findAll()).isEmpty()
    }
}
