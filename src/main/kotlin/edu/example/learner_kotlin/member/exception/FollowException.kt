package edu.example.learner_kotlin.member.exception

import org.springframework.http.HttpStatus

enum class FollowException(private val message: String, private val status: HttpStatus) {
    MEMBER_NOT_FOUND("존재하지 않는 사용자 입니다", HttpStatus.NOT_FOUND),
    FOLLOW_FAILED("팔로우에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    FOLLOWER_NOT_FOUND("팔로워를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FOLLOWING_NOT_FOUND("팔로잉를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_UPLOAD_IMAGE("이미지 업로드에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일 입니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임 입니다.", HttpStatus.BAD_REQUEST),
    NOT_REMOVE_IMAGE("이미지를 삭제하는데 실패하였습니다.", HttpStatus.BAD_REQUEST);


    val memberTaskException: MemberTaskException
        get() = MemberTaskException(this.message, status.value())
}