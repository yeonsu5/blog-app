package com.kotlin.blog.user.repository

import com.kotlin.blog.user.domain.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

// @SpringBootTest
// @ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
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

        // when
        userRepository.save(User(email, password, nickname))

        // then
        val savedUser = userRepository.findByEmail(email)
        assertThat(savedUser).isNotNull
        assertThat(email).isEqualTo(savedUser!!.email)
        assertThat(nickname).isEqualTo(savedUser.nickname)
    }
}
