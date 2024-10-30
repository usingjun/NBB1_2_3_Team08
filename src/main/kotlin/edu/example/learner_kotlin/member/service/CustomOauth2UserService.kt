package edu.example.learner_kotlin.member.service
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.oauth2.CustomOauth2User
import edu.example.learner_kotlin.member.dto.oauth2.GoogleResponse
import edu.example.learner_kotlin.member.dto.oauth2.NaverResponse
import edu.example.learner_kotlin.member.dto.oauth2.OAuth2Response
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.exception.MemberException
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOauth2UserService(private val memberRepository: MemberRepository) : DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        log.info("loadUser called")

        val oAuth2User: OAuth2User = super.loadUser(userRequest)
        log.info(oAuth2User.attributes)

        val registrationId: String = userRequest.clientRegistration.registrationId

        var oAuth2Response: OAuth2Response? = null

        if (registrationId == "naver") {
            log.info("naver")
            oAuth2Response = NaverResponse(oAuth2User.attributes)
        } else if (registrationId == "google") {
            log.info("google")
            oAuth2Response = GoogleResponse(oAuth2User.attributes)
        } else {
            log.info("Not valid registrationId")
            throw OAuth2AuthenticationException("Not valid registrationId")
        }

        val existMember: Member = memberRepository.getMemberByEmail(oAuth2Response.getEmail())
            ?: run{ val member: Member = Member().apply {
                email = oAuth2Response.getEmail()
                nickname = oAuth2Response.getNickName()
                phoneNumber = oAuth2Response.getPhoneNumber()
                role = (Role.USER)
                password = (oAuth2Response.getProviderId() + " " + oAuth2Response.getProvider())
                }
                memberRepository.save(member)
            }


            existMember.apply {
                email = oAuth2Response.getEmail()
            }

            memberRepository.save(existMember)


        val member = memberRepository.getMemberByEmail(oAuth2Response.getEmail()) ?:
            throw MemberException.MEMBER_NOT_FOUND.memberTaskException



        return CustomOauth2User(oAuth2Response, member.role.toString(), member.memberId)
    }
}
