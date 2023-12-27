package com.kotlin.blog.auth.oauth

import com.kotlin.blog.user.domain.vo.UserRegisterVo
import com.kotlin.blog.user.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserCustomService(
    private val userRepository: UserRepository,
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
        // {sub=114534781181394691499, name=오연수, given_name=연수, family_name=오,
        // picture=https://lh3.googleusercontent.com/a/ACg8ocKN_jWLptT82xSB7XlefXgaUGYs-emG-ZhiW7pxAaNS=s96-c,
        // email=lilylemonoh@gmail.com, email_verified=true, locale=ko}

        val email = attributes["email"] as String
        val name = attributes["name"] as String

        // 사용자가 이미 존재하는지 확인
        val existingUser = userRepository.findByEmail(email)

        if (existingUser == null) {
            // 사용자가 존재하지 않는 경우 데이터베이스에 저장(회원가입)
            val userRegisterVo = UserRegisterVo(email = email, password = null, nickname = name)

            userRepository.register(
                userRegisterVo.email,
                userRegisterVo.password,
                userRegisterVo.createdAt,
                userRegisterVo.nickname,
                userRegisterVo.role,
            )
        }
        // 사용자가 이미 존재하는 경우, 아무것도 하지 않음
    }
}
