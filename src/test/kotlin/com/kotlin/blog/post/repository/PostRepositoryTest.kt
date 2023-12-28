package com.kotlin.blog.post.repository

import com.kotlin.blog.post.domain.entity.Post
import com.kotlin.blog.post.domain.vo.PostSaveVo
import com.kotlin.blog.post.domain.vo.PostUpdateVo
import com.kotlin.blog.user.domain.entity.User
import com.kotlin.blog.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

// @DataJpaTest
//@ActiveProfiles("test")
@SpringBootTest
class PostRepositoryTest @Autowired constructor(
    val postRepository: PostRepository,
    val userRepository: UserRepository,
) {

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("전체 글 조회 테스트(레포지토리)")
    fun findAllTest() {
        // given
        val user = User("abc@gmail.com", "password", "nickname")
        userRepository.save(user)
        val post1 = Post("test title 1", "test content 1", user)
        val post2 = Post("test title 2", "test content 2", user)
        postRepository.save(post1)
        postRepository.save(post2)
        val pageable = PageRequest.of(0, 10)
        // when
        val results = postRepository.findAllPosts(pageable)
        // then
        assertThat(results.content).hasSize(2)
        assertThat(results.totalElements).isEqualTo(2)
        assertThat(results.content[1].title).isEqualTo("test title 2")
    }

    @Test
    @DisplayName("글 1개 조회 테스트(레포지토리)")
    fun findByIdTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = Post("test title", "test content", user)
        postRepository.save(post)
        // when
        val result = postRepository.findPostById(post.id)
        // then
        assertThat(result).isNotNull
        assertThat(result.title).isEqualTo(post.title)
        assertThat(result.content).isEqualTo(post.content)
    }

    @Test
    @DisplayName("글 저장 테스트(레포지토리)")
    fun saveTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val vo = PostSaveVo("test title", "test content", user.id)
        // when
//        postRepository.save(post)
        postRepository.savePost(vo.title, vo.content, vo.createdAt, vo.userId)
        // then
        val findAll = postRepository.findAll()
        assertThat(findAll).hasSize(1)
        assertThat(findAll[0].title).isEqualTo("test title")
    }

    @Test
    @DisplayName("글 수정 테스트(레포지토리)")
    fun updateTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = Post("initial title", "initial content", user)
        val savedPost = postRepository.save(post)
        val updatedTitle = "updated title"
        val updatedContent = "updated content"
        val updatedAt = LocalDateTime.now()

        val vo = PostUpdateVo(savedPost.id, updatedTitle, updatedContent, updatedAt)

        // when
        postRepository.updatePost(vo.id, vo.title, vo.content, vo.updatedAt)

        // then
        val findAll = postRepository.findAll()
        assertThat(findAll).hasSize(1)
        assertThat(findAll[0].title).isEqualTo("updated title")
    }

    @Test
    @DisplayName("글 삭제 테스트(레포지토리)")
    fun deleteTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = Post("to be deleted post title", "to be deleted post content", user)
        val savedPost = postRepository.save(post)
        // when
        postRepository.deleteById(savedPost.id)
        // then
        assertThat(postRepository.findById(savedPost.id)).isEmpty()
        assertThat(postRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("키워드 검색 (레포지토리)")
    fun searchPostsTest() {
        // given
        val user = User("abc@gmail.com", "password", "nickname")
        userRepository.save(user)
        val post1 = Post("apple bear carrot", "dummy data for testing", user)
        val post2 = Post("title for test", "테스트를 위한 본문", user)
        val post3 = Post("AppLe BeAr car", "본문입니다", user)
        postRepository.save(post1)
        postRepository.save(post2)
        postRepository.save(post3)
        val pageable = PageRequest.of(0, 10)

        // when (1. 한글 검색, 2. 영문 검색, 3. 대소문자 구분 없이 검색되는지 확인)
        val resultForKorean = postRepository.searchPosts("본문", pageable)

        val resultForSimpleSearch = postRepository.searchPosts("test", pageable)

        val resultsForIgnoringCase = postRepository.searchPosts("appLe", pageable)

        // then
        assertThat(resultForKorean.content).hasSize(2)
        assertThat(resultForKorean.content).extracting("title")
            .containsExactlyInAnyOrder("title for test", "AppLe BeAr car")

        assertThat(resultForSimpleSearch.content).hasSize(2)
        assertThat(resultForSimpleSearch.content).extracting("title")
            .containsExactlyInAnyOrder("apple bear carrot", "title for test")

        assertThat(resultsForIgnoringCase.content).hasSize(2)
        assertThat(resultsForIgnoringCase.content).extracting("title")
            .containsExactlyInAnyOrder("apple bear carrot", "AppLe BeAr car")
    }
}
