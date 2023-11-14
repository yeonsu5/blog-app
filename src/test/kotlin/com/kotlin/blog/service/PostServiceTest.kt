package com.kotlin.blog.service

import com.kotlin.blog.domain.Post
import com.kotlin.blog.dto.request.PostSaveRequest
import com.kotlin.blog.dto.request.PostUpdateRequest
import com.kotlin.blog.repository.PostRepository
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
class PostServiceTest @Autowired constructor(
    private val postRepository: PostRepository,
    private val postService: PostService,
) {

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("전체 글 조회(서비스)")
    fun getAllPostsTest() {
        // given
        postRepository.saveAll(
            listOf(
                Post("post 1", "post 1 content"),
                Post("post 2", "post 2 content"),
            ),
        )
        // when
        val results = postService.getAllPosts()
        // then
        assertThat(results).hasSize(2)
        assertThat(results[0].title).isEqualTo("post 1")
        assertThat(results[1].content).isEqualTo("post 2 content")
        assertThat(results).extracting("title").containsExactly("post 1", "post 2")
        assertThat(results).extracting("content").containsExactly("post 1 content", "post 2 content")
    }

    @Test
    @DisplayName("Id로 글 한개 조회(서비스)")
    fun getPostByIdTest() {
        // given
        val savedPost = postRepository.save(Post("title", "content"))
        // when
        val foundPost = postService.getPostById(savedPost.id!!)
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
        val post = PostSaveRequest(title, content)
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
        val post = postRepository.save(Post("original title", "original content"))
        val updatedTitle = "updated title"
        val updatedContent = "updated content"
        // when
        val updatedPost = postService.updatePost(post.id!!, PostUpdateRequest(updatedTitle, updatedContent))
        // then
        assertThat(post.id!!).isEqualTo(updatedPost.id)
        assertThat(updatedPost.title).isEqualTo("updated title")
        assertThat(updatedPost.content).isEqualTo("updated content")
        assertThat(post).isNotEqualTo(updatedPost)
        assertThat(updatedPost.updatedAt).isAfter(post.updatedAt)
    }

    @Test
    @DisplayName("글 삭제(서비스)")
    fun deletePostTest() {
        // given
        val post = postRepository.save(Post("title", "content"))
        // when
        postService.deletePost(post.id!!)
        // then
        val allPosts = postRepository.findAll()
        assertThat(allPosts.size).isEqualTo(0)
        assertThat(allPosts).isEmpty()
    }
}
