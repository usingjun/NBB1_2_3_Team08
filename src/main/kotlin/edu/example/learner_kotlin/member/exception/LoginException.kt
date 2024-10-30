package edu.example.learner_kotlin.member.exception

import org.springframework.http.HttpStatus

enum class LoginException(private val message: String, private val status: HttpStatus) {
    LOGIN_FAIL("로그인에 실패하였습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_EMAIL("존재하지 않는 이메일입니다.", HttpStatus.NOT_FOUND),
    PASSWORD_DISAGREEMENT("비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND),
    LOGOUT_FAIL("로그아웃에 실패하였습니다.", HttpStatus.BAD_REQUEST);


    val memberTaskException: LoginTaskException
        get() = LoginTaskException(this.message, status.value())
}
