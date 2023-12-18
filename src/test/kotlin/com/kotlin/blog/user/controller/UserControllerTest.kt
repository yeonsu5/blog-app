package com.kotlin.blog.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.user.domain.entity.User
import com.kotlin.blog.user.dto.request.UserRegisterRequest
import com.kotlin.blog.user.repository.UserRepository
import com.kotlin.blog.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    private fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("회원가입을 할 수 있다.")
    fun registerTest() {
        // given
        val request = UserRegisterRequest("user@email.com", "password123!@#", "password123!@#", "nickname")

        val requestBody = objectMapper.writeValueAsString(request)

        // when & then
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody),
        )
            .andExpect(status().isCreated)

        val user = userRepository.findByEmail("user@email.com")
        assertThat(user).isNotNull
        if (user != null) {
            assertThat(user.nickname).isEqualTo("nickname")
        }
    }

    @Test
    @DisplayName("회원가입 시 확인 비밀번호가 틀리면 예외가 발생한다.")
    fun registerConfirmPasswordTest() {
        val exception = assertThrows<InvalidInputException> {
            UserRegisterRequest("user@email.com", "password123!@#", "12345678910", "nickname")
        }

        assertThat(exception.message).isEqualTo("비밀번호와 확인 비밀번호가 일치하지 않습니다.")
    }

    // 닉네임이 중복이면 예외 발생
    @Test
    @DisplayName("회원가입 시 닉네임이 중복이면 예외가 발생한다.")
    fun registerDuplicateNicknameTest() {
        // given
        userRepository.save(User("test@email.com", "password~!@", "nickname"))

        val request = UserRegisterRequest("user@email.com", "password123!@#", "password123!@#", "nickname")

        val requestBody = objectMapper.writeValueAsString(request)

        // when & then
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody),
        )
            .andExpect(status().isBadRequest)
    }
}
