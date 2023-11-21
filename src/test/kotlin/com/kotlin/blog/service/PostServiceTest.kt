package com.kotlin.blog.service

import com.kotlin.blog.domain.Post
import com.kotlin.blog.domain.User
import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.dto.request.SortingRequest
import com.kotlin.blog.repository.PostRepository
import com.kotlin.blog.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest @Autowired constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postService: PostService,
) {

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

//    @Test
//    fun InputDummyDataForPaging() {
//        for (i in 1..500) {
//            var title = "테스트용 데이터 제목입니다:[$i]"
//            var content = "테스트용 데이터 내용입니다: [$i]"
//            postService.savePost(PostSaveRequest(title, content, 2))
//        }
//    }

    @Test
    @DisplayName("전체 글 조회(서비스)")
    fun getAllPostsTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        postRepository.saveAll(
            listOf(
                Post("post 1", "post 1 content", user),
                Post("post 2", "post 2 content", user),
            ),
        )
        val sortingRequest = SortingRequest("id", "desc")
        // when
        val results = postService.getAllPosts(0, sortingRequest)
        // then
        assertThat(results.content).hasSize(2)
        assertThat(results.content[0].title).isEqualTo("post 2")
        assertThat(results.content).extracting("title").containsExactlyInAnyOrder("post 1", "post 2")
    }

    @Test
    @DisplayName("Id로 글 한개 조회(서비스)")
    fun getPostByIdTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val savedPost = postRepository.save(Post("title", "content", user))
        // when
        val foundPost = postService.getPostById(savedPost.id)
        // then
        assertThat(foundPost).isNotNull
        assertThat(foundPost.title).isEqualTo("title")
        assertThat(foundPost.content).isEqualTo("content")
    }

    @Test
    @DisplayName("글 저장(서비스)")
    fun savePostTest() {
        // given
        val title = "title"
        val content = "content"
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = PostSaveRequest(title, content, user.id)
        // when
        postService.savePost(post)
        // then
        val allPosts = postRepository.findAll()
        assertThat(allPosts).hasSize(1)
        assertThat(allPosts[0].title).isEqualTo(title)
        assertThat(allPosts[0].content).isEqualTo(content)
    }

    @Test
    @DisplayName("글 수정(서비스)")
    fun updatePostTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = postRepository.save(Post("original title", "original content", user))
        val updatedTitle = "updated title"
        val updatedContent = "updated content"
        // when
        val updatedPost = postService.updatePost(post.id, PostUpdateRequest(updatedTitle, updatedContent))
        // then
        assertThat(post.id).isEqualTo(updatedPost.id)
        assertThat(updatedPost.title).isEqualTo("updated title")
        assertThat(updatedPost.content).isEqualTo("updated content")
        assertThat(post).isNotEqualTo(updatedPost)
        assertThat(updatedPost.updatedAt).isAfter(post.createdAt)
    }

    @Test
    @DisplayName("글 삭제(서비스)")
    fun deletePostTest() {
        // given
        val user = userRepository.save(User("abc@gmail.com", "password", "nickname"))
        val post = postRepository.save(Post("title", "content", user))
        // when
        postService.deletePostById(post.id)
        // then
        val allPosts = postRepository.findAll()
        assertThat(allPosts.size).isEqualTo(0)
        assertThat(allPosts).isEmpty()
    }
}
