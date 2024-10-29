package edu.example.learner.member.dto;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    private String email;
    private String password;
    private Cookie cookie;
    private Long memberId;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginDTO(Cookie cookie, Long memberId) {
        this.cookie = cookie;
        this.memberId = memberId;
    }
}
