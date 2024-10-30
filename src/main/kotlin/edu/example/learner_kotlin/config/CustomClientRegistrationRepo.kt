package edu.example.learner_kotlin.config

import edu.example.learner_kotlin.member.dto.oauth2.SocialClientRegistration
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository

@Configuration
class CustomClientRegistrationRepo(
    private val socialClientRegistration: SocialClientRegistration
) {
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        //인메모리 형식으로 naver 및 google 클라이언트 정보 저장
        return InMemoryClientRegistrationRepository(
            socialClientRegistration.naverClientRegistration(),
            socialClientRegistration.googleClientRegistration()
        )
    }
}
