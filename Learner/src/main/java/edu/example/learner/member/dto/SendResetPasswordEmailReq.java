package edu.example.learner.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendResetPasswordEmailReq {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력하세요.")
    private String email;

    public SendResetPasswordEmailReq(String email) {
        this.email = email;
    }
}