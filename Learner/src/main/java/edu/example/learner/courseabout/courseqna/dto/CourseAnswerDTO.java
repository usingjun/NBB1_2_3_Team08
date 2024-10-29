package edu.example.learner.courseabout.courseqna.dto;

import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.member.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseAnswerDTO {
    private Long answerId;

    @NotEmpty
    private String answerContent;

    private LocalDateTime answerCreateDate;

    private Long inquiryId;
    private Long memberId;
    private String memberNickname;     // 작성자 닉네임
    private byte[] profileImage;       // 작성자 프로필 이미지

    public CourseAnswerDTO(CourseAnswer answer) {
        this.answerId = answer.getAnswerId();
        this.answerContent = answer.getAnswerContent();
        this.answerCreateDate = answer.getAnswerCreateDate();
        this.inquiryId = answer.getCourseInquiry().getInquiryId();
        this.memberId = answer.getMember().getMemberId();
        this.memberNickname = answer.getMember().getNickname();
        this.profileImage = answer.getMember().getProfileImage();
    }

    public CourseAnswer toEntity() {
        return CourseAnswer.builder()
                .answerId(answerId)
                .member(Member.builder()
                        .memberId(memberId)
                        .nickname(memberNickname)
                        .profileImage(profileImage)
                        .build())
                .answerContent(answerContent)
                .answerCreateDate(answerCreateDate)
                .courseInquiry(CourseInquiry.builder().inquiryId(inquiryId).build())
                .build();
    }
}
