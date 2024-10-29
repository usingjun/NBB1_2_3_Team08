package edu.example.learner.courseabout.course.service;

import edu.example.learner.courseabout.course.entity.MemberCourse;
import edu.example.learner.courseabout.course.repository.MemberCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.exception.CourseException;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.exception.MemberException;
import edu.example.learner.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class MemberCourseService {

    private final MemberCourseRepository memberCourseRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    // 구매 여부 확인
    public boolean checkPurchase(Long courseId, Long memberId) {
        // 구매 기록이 있는지 확인
        MemberCourse purchase = memberCourseRepository.findByCourseIdAndMemberId(courseId, memberId);

        return purchase != null;
    }

    @Transactional
    public MemberCourse purchaseCourse(Long memberId, Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
        MemberCourse memberCourse = MemberCourse.builder()
                .member(member) // 위에서 가져온 member
                .course(course) // 위에서 가져온 course
                .purchaseDate(new Date())
                .build();

        return memberCourseRepository.save(memberCourse);
    }
}
