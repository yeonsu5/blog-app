package com.kotlin.blog.user.service

// import com.kotlin.blog.common.authority.JwtTokenUtil
import com.kotlin.blog.common.exception.InvalidInputException
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun register(userRegisterVo: UserRegisterVo) {
        validateDuplicateEmail(userRegisterVo.email)
        validateDuplicateNickname(userRegisterVo.nickname)

        userRepository.register(
            userRegisterVo.email,
//            userRegisterVo.password,
            encoder.encode(userRegisterVo.password),
            userRegisterVo.createdAt,
            userRegisterVo.nickname,
            userRegisterVo.role,
        )
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
