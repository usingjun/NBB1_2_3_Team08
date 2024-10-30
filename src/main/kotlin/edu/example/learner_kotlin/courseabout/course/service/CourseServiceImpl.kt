package edu.example.learner_kotlin.courseabout.course.service

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.courseabout.course.dto.CourseDTO
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.exception.CourseException
import edu.example.learner_kotlin.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
@Transactional
class CourseServiceImpl(
    private val modelMapper: ModelMapper,
    private val courseRepository: CourseRepository,
//    private val memberCourseRepository: MemberCourseRepository,
    private val memberRepository: MemberRepository
) : CourseService {

    override fun addCourse(courseDTO: CourseDTO): CourseDTO {
        log.info("add course")
        val member = memberRepository.getMemberByNickName(courseDTO.memberNickname!!)

        log.info("member: {$member}")
        val course = Course().apply {
            this.courseDescription=courseDTO.courseDescription
            this.courseLevel=courseDTO.courseLevel
            this.courseName = courseDTO.courseName
            this.courseAttribute = CourseAttribute.fromString(courseDTO.courseAttribute!!);
            this.coursePrice=courseDTO.coursePrice
            this.member=member
        }
        courseRepository.save(course)

        log.info("successfully added course")
        return courseDTO
    }

    override fun read(courseId: Long): CourseDTO {
        val course = courseRepository.findById(courseId)
            .orElseThrow { CourseException.COURSE_NOT_FOUND.courseException }

        log.info("course: {}", course)
        return CourseDTO(course)
    }

    override fun readByAttribute(courseAttribute: CourseAttribute): List<CourseDTO> {
        val courseList = courseRepository.readByCourseAttribute(courseAttribute)

        log.info("successfully read course list")
        return courseList.map { CourseDTO(it!!) }
    }

    @Transactional
    override fun readReview(courseId: Long): CourseDTO {
        val course = courseRepository.findByIdWithMember(courseId)
            .orElseThrow { RuntimeException("Course not found") }
        return CourseDTO(course)
    }

    override fun updateCourse(courseDTO: CourseDTO): CourseDTO {
        val course = courseRepository.findById(courseDTO.courseId!!)
            .orElseThrow { CourseException.MEMBER_COURSE_NOT_FOUND.courseException }

        try {
            with(course) {
                courseName = courseDTO.courseName
                courseLevel = courseDTO.courseLevel
                coursePrice = courseDTO.coursePrice
                courseDescription = courseDTO.courseDescription
                courseAttribute = CourseAttribute.fromString(courseDTO.courseAttribute!!)
            }
            log.info("저장된 데이터 확인: {}", course)
        } catch (e: Exception) {
            log.error("Error updating course: ", e)
            throw CourseException.COURSE_NOT_MODIFIED.courseException
        }
        return courseDTO
    }

    override fun deleteCourse(courseId: Long) {
        try {
            courseRepository.deleteById(courseId)
        } catch (e: Exception) {
            log.error("Error deleting course: ", e)
            log.error("courseId: {}", courseId)
            throw CourseException.COURSE_NOT_DELETED.courseException
        }
    }

    override fun readAll(): List<CourseDTO> {
        val courseList = courseRepository.findAll()
        return courseList.map { CourseDTO(it) }
    }

    override fun getCoursesByNickname(nickname: String): List<CourseDTO> {
        TODO("Not yet implemented")
    }

//    override fun getCoursesByNickname(nickname: String): List<CourseDTO> {
//        val byMemberNickname = courseRepository.getByMemberNickname(nickname)
//        return byMemberNickname.map { CourseDTO(it) } ?: emptyList() // 빈 리스트 반환
//    }
//
//    override fun getMemberCoursesByMemberId(memberId: Long): List<MemberCourseDTO> {
//        val memberCourseList = memberCourseRepository.getMemberCourse(memberId)
//            ?: throw CourseException.MEMBER_COURSE_NOT_FOUND.courseException
//
//        return memberCourseList.map { MemberCourseDTO(it) }.also {
//            log.info("Member courses: {}", it)
//        }
//    }
//
//    override fun getCoursesByMemberId(memberId: Long): List<CourseDTO> {
//        val memberCourses = memberCourseRepository.findByMember_MemberId(memberId)
//            .takeIf { it!!.isNotEmpty() } ?: throw CourseException.MEMBER_COURSE_NOT_FOUND.courseException
//
//        return memberCourses.map { CourseDTO(it?.course!!) }
//    }

    // 강사 닉네임 반환
//    fun getInstructorNicknameByCourseId(courseId: Long): String {
//        val course = courseRepository.findById(courseId)
//            .orElseThrow { IllegalArgumentException("해당 코스를 찾을 수 없습니다.") }
//
//        return course.member.nickname
//    }
}
