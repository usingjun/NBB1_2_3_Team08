package edu.example.learner.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendResetPasswordEmailRes {
    private String uuid;

    @Builder
    public SendResetPasswordEmailRes(String uuid) {
        this.uuid = uuid;
    }
}
