package com.kotlin.blog.user.service

import com.kotlin.blog.common.authority.Role
import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.user.domain.vo.UserLoginVo
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.UserRepository
import com.kotlin.blog.user.repository.UserRoleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest @Autowired constructor(
    val userService: UserService,
    val userRepository: UserRepository,
    val userRoleRepository: UserRoleRepository,
    val passwordEncoder: PasswordEncoder,
) {

    @AfterEach
    fun clean() {
        userRoleRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("회원가입 성공(서비스)")
    @Transactional
    fun registerTest_Success() {
        // given
        val userRegisterVo = UserRegisterVo("new@email.com", "password12!@", "nick", LocalDateTime.now())
        // when
        userService.register(userRegisterVo)
        // then
        val savedUser = userRepository.findByEmail("new@email.com")
        assertThat(savedUser).isNotNull
        assertThat("new@email.com").isEqualTo(savedUser!!.email)
        assertThat("nick").isEqualTo(savedUser.nickname)
        assertThat(passwordEncoder.matches("password12!@", savedUser.password))
        assertThat(savedUser.userRole.first().role.name).isEqualTo(Role.MEMBER.name)
    }

    @Test
    @DisplayName("회원가입 중복이메일(서비스)")
    fun registerTest_Fail_DuplicateEmail() {
        // given
        val existingEmail = "existing@email.com"
        val userRegisterVo = UserRegisterVo(existingEmail, "password12!@", "nick", LocalDateTime.now())
        userService.register(userRegisterVo)

        val duplicateEmailUserRegisterVo =
            UserRegisterVo(existingEmail, "password12!3@", "nickname", LocalDateTime.now())

        // when & then
        assertThrows<InvalidInputException> { userService.register(duplicateEmailUserRegisterVo) }
    }

    @Test
    @DisplayName("회원가입 중복닉네임(서비스)")
    fun registerTest_Fail_DuplicateNickname() {
        // given
        val existingNickname = "nickname"
        val userRegisterVo = UserRegisterVo("user@email.com", "password123!@", existingNickname, LocalDateTime.now())
        userService.register(userRegisterVo)

        val duplicateNicknameUserRegisterVo =
            UserRegisterVo("another@email.com", "password123!@", existingNickname, LocalDateTime.now())

        // when & then
        assertThrows<InvalidInputException> { userService.register(duplicateNicknameUserRegisterVo) }
    }

    @Test
    @DisplayName("로그인 테스트")
    fun loginTest() {
        // given
        val email = "test@email.com"
        val password = "password12!@"
        val userRegisterVo = UserRegisterVo(email, password, "nick", LocalDateTime.now())
        userService.register(userRegisterVo)

        val userLoginVo = UserLoginVo(email, password)

        // when
        val toKenInfo = userService.login(userLoginVo)

        // then
        assertThat(toKenInfo).isNotNull
        assertThat(toKenInfo.grantType).isEqualTo("Bearer")
    }
}
