package edu.example.learner.courseabout.course.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.example.learner.courseabout.course.entity.MemberCourse;
import edu.example.learner.courseabout.course.entity.QMemberCourse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberCourseSearchImpl implements MemberCourseSearch {
    private final JPAQueryFactory queryFactory;
    private final QMemberCourse QmemberCourse = QMemberCourse.memberCourse1;


    @Override
    public List<MemberCourse> getMemberCourse(Long memberId) {
        return queryFactory
                .selectFrom(QmemberCourse)
                .where(QmemberCourse.member.memberId.eq(memberId))
                .fetch();
    }
}
