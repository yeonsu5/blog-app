package com.kotlin.blog.post.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.blog.auth.jwt.JwtTokenUtil
import com.kotlin.blog.post.domain.entity.Post
import com.kotlin.blog.post.dto.request.PostSaveRequest
import com.kotlin.blog.post.dto.request.PostUpdateRequest
import com.kotlin.blog.post.repository.PostRepository
import com.kotlin.blog.user.domain.entity.Role
import com.kotlin.blog.user.domain.entity.User
import com.kotlin.blog.user.repository.RoleRepository
import com.kotlin.blog.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.* // ktlint-disable no-wildcard-imports
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.* // ktlint-disable no-wildcard-imports
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @AfterEach
    fun clear() {
        userRepository.deleteAll()
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("전체 글 디폴트 조회 시 post id 기준 역순으로 정렬된다.")
    fun findAllDefaultTest() {
        // given
        createDefaultUserAndPosts()

        // when & then
        mockMvc.perform(get("/api/posts"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content[0].title").value("title 2"))
            .andExpect(jsonPath("$.data.content[1].title").value("title 1"))
    }

    @Test
    @DisplayName("전체 글 조회 시 정렬 기준 추가하여 조회할 수 있다.")
    fun findAllSortingAndOrderingTest() {
        // given
        createDefaultUserAndPosts()

        // when & then (title 기준 오름차순 조회)
        mockMvc.perform(get("/api/posts").param("sortBy", "title").param("orderBy", "asc"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content[0].title").value("title 1"))
            .andExpect(jsonPath("$.data.content[1].title").value("title 2"))
    }

    @Test
    @DisplayName("id로 post를 조회할 수 있다.")
    fun findPostByIdTest() {
        // given
        val user = userRepository.save(User("use1r@email.com", "password", "nickname"))
        val post = postRepository.save(Post("title", "content", user))

        // when & then
        mockMvc.perform(get("/api/posts/${post.id}"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.title").value("title"))
            .andExpect(jsonPath("$.data.content").value("content"))
            .andExpect(jsonPath("$.data.userNickname").value("nickname"))
    }

    @Test
    @DisplayName("글쓰기를 할 수 있다.")
    fun createPostTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        roleRepository.save(Role("USER", user))
        val request = PostSaveRequest("new title", "new content", user.id)

        val requestBody = objectMapper.writeValueAsString(request)

        val jwtToken = createJwtToken(user.email)

        // when & then
        mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody),
        )
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.data").isEmpty)

        val findAll = postRepository.findAll()
        assertThat(findAll).size().isEqualTo(1)
        assertThat(findAll[0].title).isEqualTo("new title")
        assertThat(findAll[0].content).isEqualTo("new content")
    }

    @Test
    @DisplayName("Jwt 토큰 없이 글쓰기를 하려 하면 예외가 발생한다.")
    fun createPostWithoutJwtTokenTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        val request = PostSaveRequest("new title", "new content", user.id)

        val requestBody = objectMapper.writeValueAsString(request)

        // when & then
        mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("id로 글 삭제를 할 수 있다.")
    fun deletePostTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        roleRepository.save(Role("USER", user))
        val post = postRepository.save(Post("title", "content", user))

        val jwtToken = createJwtToken(user.email)

        // when & then
        mockMvc.perform(
            delete("/api/posts/${post.id}")
                .header("Authorization", "Bearer $jwtToken"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)

        val findAll = postRepository.findAll()
        assertThat(findAll).isEmpty()
    }

    @Test
    @DisplayName("없는 id로 글 삭제를 하려 하면 예외가 발생한다.")
    fun deletePostNonExistingIdTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        roleRepository.save(Role("USER", user))
        val post = postRepository.save(Post("title", "content", user))

        val jwtToken = createJwtToken(user.email)

        // when & then
        mockMvc.perform(
            delete("/api/posts/100")
                .header("Authorization", "Bearer $jwtToken"),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("id로 글 수정을 할 수 있다.")
    fun updatePostTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        roleRepository.save(Role("USER", user))
        val post = postRepository.save(Post("title", "content", user))

        val request = PostUpdateRequest("update title", "update content")

        val requestBody = objectMapper.writeValueAsString(request)

        val jwtToken = createJwtToken(user.email)

        // when & then
        mockMvc.perform(
            put("/api/posts/${post.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)

        val findAll = postRepository.findAll()
        assertThat(findAll[0].title).isEqualTo("update title")
        assertThat(findAll[0].content).isEqualTo("update content")
    }

    @Test
    @DisplayName("키워드로 검색을 할 수 있다.")
    fun searchPostByKeywordTest() {
        // given
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        postRepository.save(Post("apple banana", "search test", user))
        postRepository.save(Post("search works", "ignore case", user))
        postRepository.save(Post("and it works", "both title and content", user))
        postRepository.save(Post("search test", "aPpLe", user))

        // when & then
        mockMvc.perform(
            get("/api/posts/search")
                .param("keyword", "apple"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.content[0].title").value("search test"))
            .andExpect(jsonPath("$.data.content[1].title").value("apple banana"))
            .andExpect(jsonPath("$.data.content.size()").value(2))
    }

    private fun createDefaultUserAndPosts() {
        val user = userRepository.save(User("user@email.com", "password", "nickname"))
        postRepository.saveAll(
            listOf(
                Post("title 1", "content 1", user, id = 1L),
                Post("title 2", "content 2", user, id = 2L),
            ),
        )
    }

    private fun createJwtToken(email: String): String {
        val userDetails = userDetailsService.loadUserByUsername(email)

        return jwtTokenUtil.generate(userDetails, Date(System.currentTimeMillis() + (1000 * 60)))
    }
}
