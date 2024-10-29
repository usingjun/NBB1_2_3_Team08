package edu.example.learner.member.exception;

import org.springframework.http.HttpStatus;

public enum MemberException {
    MEMBER_NOT_FOUND("존재하지 않는 사용자 입니다", HttpStatus.NOT_FOUND),
    MEMBER_NOT_REGISTERED("회원가입에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_MODIFIED("회원정보수정에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_DELETE("회원탈퇴에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    NOT_UPLOAD_IMAGE("이미지 업로드에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일 입니다.",HttpStatus.BAD_REQUEST),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임 입니다.",HttpStatus.BAD_REQUEST),
    NOT_REMOVE_IMAGE("이미지를 삭제하는데 실패하였습니다.", HttpStatus.BAD_REQUEST),;


    private String message;
    private HttpStatus status;

    MemberException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public MemberTaskException getMemberTaskException() {
        return new MemberTaskException(this.message,this.status.value());
    }
}
