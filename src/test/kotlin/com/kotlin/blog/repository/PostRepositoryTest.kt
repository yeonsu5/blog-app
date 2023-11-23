package com.kotlin.blog.repository

import com.kotlin.blog.domain.entity.Post
import com.kotlin.blog.domain.entity.User
import com.kotlin.blog.domain.vo.PostSaveVo
import com.kotlin.blog.domain.vo.PostUpdateVo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostRepositoryTest(
    @Autowired val postRepository: PostRepository,
    @Autowired val userRepository: UserRepository,
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
        assertThat(result?.title).isEqualTo(post.title)
        assertThat(result?.content).isEqualTo(post.content)
    }

    @Test
    @DisplayName("글 저장 테스트(레포지토리)")
    fun saveTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = Post("test title", "test content", user)
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
        postRepository.save(post)
        val updatedTitle = "updated title"
        val updatedContent = "updated content"

        val vo = PostUpdateVo(post.id, updatedTitle, updatedContent)

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
        /*
        !! 연산자를 사용하여 savedPost.id가 null일 경우 NullPointerException을 던지도록 강제하였다.
         테스트 코드에서는 예상치 못한 상황이 발생했을 때 즉시 테스트를 실패시키는 게 좋으므로 테스트 코드에서 이렇게 작성하는건 괜찮지만
         이런 방식의 null 처리는 실제 프로덕션 코드에서는 권장되지 않는다. 프로덕션 코드에서는 가능한 한 안전한 null-safe 방식을 사용해야 한다.
         예를 들어
         savedPost.id?.let { postRepository.deleteById(it) }
         이러한 방식으로 작성하면 savedPost.id가 null인 경우 deleteById 메소드가 호출되지 않는다.
        */

        // then
        assertThat(postRepository.findById(savedPost.id)).isEmpty()
        assertThat(postRepository.findAll()).isEmpty()
    }
}
