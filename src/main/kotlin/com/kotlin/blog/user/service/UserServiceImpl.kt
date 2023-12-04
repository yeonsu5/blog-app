package com.kotlin.blog.user.service

import com.kotlin.blog.common.authority.JwtTokenProvider
import com.kotlin.blog.common.authority.Role
import com.kotlin.blog.common.authority.TokenInfo
import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.user.domain.vo.UserLoginVo
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.UserRepository
import com.kotlin.blog.user.repository.UserRoleRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) : UserService {

    @Transactional
    override fun register(userRegisterVo: UserRegisterVo) {
        validateDuplicateEmail(userRegisterVo.email)
        validateDuplicateNickname(userRegisterVo.nickname)

        userRepository.register(
            userRegisterVo.email,
            passwordEncoder.encode(userRegisterVo.password),
            userRegisterVo.createdAt,
            userRegisterVo.nickname,
        )

        // 권한 저장
        userRepository.findByEmail(userRegisterVo.email)?.id?.let { userId ->
            userRoleRepository.saveRole(Role.MEMBER, userId)
        }
    }

    @Transactional
    override fun login(userLoginVo: UserLoginVo): TokenInfo {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(userLoginVo.email, userLoginVo.password)

        val authentication =
            authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    fun validateDuplicateEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw InvalidInputException("email", "이미 등록된 이메일입니다.")
        }
    }

    fun validateDuplicateNickname(nickName: String) {
        if (userRepository.existsByNickname(nickName)) {
            throw InvalidInputException("nickname", "이미 등록된 닉네임입니다.")
        }
    }
}
