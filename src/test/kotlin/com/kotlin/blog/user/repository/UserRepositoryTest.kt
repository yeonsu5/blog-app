package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest @Autowired constructor(
    val userRepository: UserRepository,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("회원가입(레포지토리)")
    fun registerTest() {
        // given
        val email = "test@example.com"
        val password = "password123!@"
        val createdAt = LocalDateTime.now()
        val nickname = "testUser"
        val role = Role.USER

        // when
        userRepository.register(email, password, createdAt, nickname, role)

        // then
        val savedUser = userRepository.findByEmail(email)
        assertThat(savedUser).isNotNull
        assertThat(email).isEqualTo(savedUser!!.email)
        assertThat(nickname).isEqualTo(savedUser.nickname)
    }
}
