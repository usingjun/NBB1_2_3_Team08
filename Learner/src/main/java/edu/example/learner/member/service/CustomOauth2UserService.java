package edu.example.learner.member.service;

import edu.example.learner.member.dto.Oauth2.CustomOauth2User;
import edu.example.learner.member.dto.Oauth2.GoogleResponse;
import edu.example.learner.member.dto.Oauth2.NaverResponse;
import edu.example.learner.member.dto.Oauth2.OAuth2Response;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import edu.example.learner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")) {
            log.info("naver");
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        }
        else if(registrationId.equals("google")) {
            log.info("google");
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }
        else {
            return null;
        }

        Optional<Member> existMember = memberRepository.getMemberByEmail(oAuth2Response.getEmail());
        if(existMember.isEmpty()){
            Member member = Member.builder()
                                  .email(oAuth2Response.getEmail())
                                  .nickname(oAuth2Response.getNickName() + new Random().nextInt(1000))
                                  .phoneNumber(oAuth2Response.getPhoneNumber())
                                  .role(Role.USER)
                                  .password(oAuth2Response.getProviderId() + " " + oAuth2Response.getProvider())
                                  .build();

            memberRepository.save(member);
        }
        else {
            existMember.get().changeEmail(oAuth2Response.getEmail());

            memberRepository.save(existMember.get());
        }

        Member member = memberRepository.getMemberByEmail(oAuth2Response.getEmail()).get();

        return new CustomOauth2User(oAuth2Response, String.valueOf(member.getRole()),member.getMemberId());
    }
}
