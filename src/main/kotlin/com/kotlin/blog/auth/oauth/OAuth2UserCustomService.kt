package com.kotlin.blog.auth.oauth

import com.kotlin.blog.user.domain.entity.Role
import com.kotlin.blog.user.domain.entity.SocialLogin
import com.kotlin.blog.user.domain.entity.SocialLoginProvider
import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.RoleRepository
import com.kotlin.blog.user.repository.SocialLoginRepository
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserCustomService(
    private val userRepository: UserRepository,
    private val socialLoginRepository: SocialLoginRepository,
    private val roleRepository: RoleRepository,
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        val user = super.loadUser(userRequest)
        registerUserIfNotExist(user)

        return user
    }

    private fun registerUserIfNotExist(oAuth2User: OAuth2User) {
        val attributes = oAuth2User.attributes
//        println(attributes)
        // {sub=114, name=, given_name=, family_name=,
        // picture=https://lh3.googleusercontent.com/a/ACg8ocKN_jWLptT82xSB7XlefXgaUGYs-emG-ZhiW7pxAaNS=s96-c,
        // email=li@gmail.com, email_verified=true, locale=ko}
        val email = attributes["email"] as String

        // 사용자가 이미 존재하는지 확인

        if (!userExists(email)) {
            // 사용자가 존재하지 않는 경우 데이터베이스에 저장(회원가입)
            val name = attributes["name"] as String
            val userRegisterVo = UserRegisterVo(email = email, password = null, nickname = name)
            val savedUser = userRepository.save(userRegisterVo.toEntity())

            // 권한 정보 데이터베이스에 저장 **권한이 USER라고 가정**
            roleRepository.save(Role("USER", savedUser))

            // 소셜 로그인 정보 데이터베이스에 저장
            val providerId = attributes["sub"] as String
            val provider = SocialLoginProvider.GOOGLE
            val profileImageUrl = attributes["picture"] as String

            socialLoginRepository.save(SocialLogin(providerId, provider, profileImageUrl, user = savedUser))
        }
    }

    private fun userExists(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }
}
