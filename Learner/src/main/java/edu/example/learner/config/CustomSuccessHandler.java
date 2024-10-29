package edu.example.learner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.learner.member.dto.Oauth2.CustomOauth2User;
import edu.example.learner.security.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User
        CustomOauth2User user = (CustomOauth2User) authentication.getPrincipal();
        Map<String, Object> claims = user.getAttributes();

        String token = jwtUtil.createToken(claims, 30);
        log.info("token: " + token);

        response.addCookie(createCookie("Authorization", token));

        // memberId를 가져와서 리디렉션 URL에 추가
        Long memberId = user.getMemberId();
        String redirectUrl = "http://localhost:3000/courses?memberId=" + memberId;

        response.sendRedirect(redirectUrl);
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60); // 60시간
        cookie.setPath("/"); // 전체 경로에서 접근 가능
        cookie.setHttpOnly(false); // JavaScript에서 접근 가능
        cookie.setSecure(false); // 로컬 개발 시 false로 설정

        return cookie;
    }
}
