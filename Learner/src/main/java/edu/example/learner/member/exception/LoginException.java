package edu.example.learner.member.exception;

import org.springframework.http.HttpStatus;

public enum LoginException {
    LOGIN_FAIL("로그인에 실패하였습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_EMAIL("존재하지 않는 이메일입니다.", HttpStatus.NOT_FOUND),
    PASSWORD_DISAGREEMENT("비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND),
    LOGOUT_FAIL("로그아웃에 실패하였습니다.", HttpStatus.BAD_REQUEST);


    private String message;
    private HttpStatus status;

    LoginException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public LoginTaskException getMemberTaskException() {
        return new LoginTaskException(this.message,this.status.value());
    }
}
