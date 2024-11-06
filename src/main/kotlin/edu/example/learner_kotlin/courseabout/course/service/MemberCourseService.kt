package edu.example.learner_kotlin.courseabout.course.service


import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.repository.MemberCourseRepository
import edu.example.learner_kotlin.courseabout.exception.CourseException
import edu.example.learner_kotlin.courseabout.exception.CourseTaskException
import edu.example.learner_kotlin.courseabout.exception.MemberException
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class MemberCourseService (
    private val memberCourseRepository: MemberCourseRepository,
            private val courseRepository: CourseRepository,
            private val memberRepository: MemberRepository,
){
    // 구매 여부 확인
    fun checkPurchase(courseId: Long?, memberId: Long?): Boolean {
        // 구매 기록이 있는지 확인
        val purchase = memberCourseRepository.findByCourseIdAndMember_Id(courseId!!, memberId!!)

        // 만약 구매가 없으면 예외를 던짐
        if (purchase == null) {
            throw CourseTaskException("Course not purchased for courseId: $courseId and memberId: $memberId", 404)
        }
        return true
    }

    @Transactional
    fun purchaseCourse(memberId: Long, courseId: Long): MemberCourse {
        val findCourse: Course = courseRepository.findById(courseId).orElseThrow(CourseException.COURSE_NOT_FOUND::courseException)
        val findMember: Member = memberRepository.findById(memberId).orElseThrow(MemberException.MEMBER_NOT_FOUND::memberTaskException)
        val memberCourse= MemberCourse().apply {
            member=findMember
            course=findCourse
            purchaseDate= Date()
        }

        return memberCourseRepository.save(memberCourse)
    }
}