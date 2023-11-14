package com.kotlin.blog.repository

import com.kotlin.blog.domain.Post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostRepositoryTest(
    @Autowired val postRepository: PostRepository,
) {

    @BeforeEach
    fun clean() {
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("전체 글 조회 테스트")
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
    @DisplayName("글 1개 조회 테스트")
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
    @DisplayName("글 저장 테스트")
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
    @DisplayName("글 수정 테스트")
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
    @DisplayName("글 삭제 테스트")
    fun deleteTest() {
        // given
        val post = Post("to be deleted post title", "to be deleted post content")
        val savedPost = postRepository.save(post)
        // when
        postRepository.deleteById(savedPost.id!!)
        // then
        assertThat(postRepository.findAll()).isEmpty()
    }
}
