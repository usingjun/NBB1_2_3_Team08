package edu.example.learner_kotlin.courseabout.course.repository.search

import com.querydsl.jpa.impl.JPAQueryFactory
import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.courseabout.course.entity.QMemberCourse


class MemberCourseSearchImpl(
    private val queryFactory: JPAQueryFactory ,
    private val QmemberCourse: QMemberCourse = QMemberCourse.memberCourse1
) : MemberCourseSearch {



    override fun getMemberCourse(memberId: Long?): MutableList<MemberCourse> {
        return queryFactory
            .selectFrom<MemberCourse>(QmemberCourse)
            .where(QmemberCourse.member.memberId.eq(memberId))
            .fetch()
    }
}