package com.kotlin.blog.user.service

// import com.kotlin.blog.common.authority.JwtTokenUtil
import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.user.domain.entity.Role
import com.kotlin.blog.user.domain.entity.User
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.RoleRepository
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val encoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun register(userRegisterVo: UserRegisterVo) {
        validateDuplicateEmail(userRegisterVo.email)
        validateDuplicateNickname(userRegisterVo.nickname)

        val encodedPassword = encoder.encode(userRegisterVo.password)
        val newUserRegisterVo = userRegisterVo.copy(password = encodedPassword)

        val savedUser = userRepository.save(newUserRegisterVo.toEntity())

        roleRepository.save(Role("USER", savedUser))
    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
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
