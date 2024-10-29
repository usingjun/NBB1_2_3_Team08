package edu.example.learner.courseabout.courseqna.dto;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.courseqna.entity.InquiryStatus;
import edu.example.learner.member.entity.Member;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseInquiryDTO {
    private Long inquiryId;
    private Long courseId;
    private Long memberId;
    private String inquiryTitle;
    private String inquiryContent;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private InquiryStatus inquiryStatus;
    private String memberNickname;     // 작성자 닉네임
    private byte[] profileImage;       // 작성자 프로필 이미지


    @Min(0)
    private Long inquiryGood;

    public CourseInquiry toEntity(){
        CourseInquiry courseInquiry = CourseInquiry.builder()
                .course(Course.builder().courseId(courseId).build())
                .member(Member.builder().memberId(memberId).build())
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .inquiryStatus(inquiryStatus)
                .build();

        return courseInquiry;
    }

    public CourseInquiryDTO(CourseInquiry ci){
        this.inquiryId = ci.getInquiryId();
        this.courseId = ci.getCourse().getCourseId();
        this.memberId = ci.getMember().getMemberId();
        this.memberNickname = ci.getMember().getNickname();
        this.profileImage = ci.getMember().getProfileImage();
        this.inquiryTitle = ci.getInquiryTitle();
        this.inquiryContent = ci.getInquiryContent();
        this.createdDate = ci.getCreatedDate();
        this.updateDate = ci.getUpdateTime();
        this.inquiryStatus = ci.getInquiryStatus();
        this.inquiryGood = ci.getInquiryGood();
    }
}
