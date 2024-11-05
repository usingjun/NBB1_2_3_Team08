package edu.example.learner_kotlin.courseabout.course.service

import edu.example.learner_kotlin.alarm.service.AlarmService
import edu.example.learner_kotlin.courseabout.course.dto.CourseCreateDTO
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.courseabout.course.dto.CourseDTO
import edu.example.learner_kotlin.courseabout.course.dto.CourseUpdateDTO
import edu.example.learner_kotlin.courseabout.course.dto.MemberCourseDTO
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.repository.MemberCourseRepository
import edu.example.learner_kotlin.courseabout.exception.CourseException
import edu.example.learner_kotlin.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class CourseServiceImpl(
    private val courseRepo: CourseRepository,
    private val memberCourseRepo: MemberCourseRepository,
    private val memberRepo: MemberRepository,
    private val alarmService: AlarmService,
) : CourseService {

    override fun addCourse(dto: CourseCreateDTO): CourseCreateDTO {
        val member = memberRepo.getMemberByNickName(dto.memberNickname)

        val course = CourseMapper.toEntity(dto,member!!)
        courseRepo.save(course)

        log.info("successfully added course")
        alarmService.createAlarmToUser("{${member.nickname} 님의 강의가 생성되었습니다}","강의 생성" )
        return dto
    }

    override fun read(courseId: Long): CourseDTO {
        val findByIdOrNull = courseRepo.findByIdOrNull(courseId)!!

        val course = findByIdOrNull

        log.info("course: {}", course)
        return CourseDTO(course)
    }

    override fun readByAttribute(courseAttribute: CourseAttribute): List<CourseDTO> =
        courseRepo.readByCourseAttribute(courseAttribute).map { CourseDTO(it!!) }.also {  log.info("successfully read course list") }


    @Transactional
    override fun readReview(courseId: Long): CourseDTO {
        val course = courseRepo.findByIdWithMember(courseId)
            .orElseThrow { RuntimeException("Course not found") }
        return CourseDTO(course)
    }

    override fun updateCourse(dto: CourseUpdateDTO): CourseUpdateDTO {
        val course = courseRepo.findById(dto.courseId)
            .orElseThrow { CourseException.MEMBER_COURSE_NOT_FOUND.courseException }
        try {
            val updateCourse= CourseMapper.toUpdateEntity(dto,course)
            courseRepo.save(updateCourse)
            log.info("저장된 데이터 확인: {$course}")

    } catch (e: Exception) {
            log.error("Error updating course: ", e)
            throw CourseException.COURSE_NOT_MODIFIED.courseException
        }
        return dto
    }

    override fun deleteCourse(courseId: Long) {
        try {
            courseRepo.deleteById(courseId)
        } catch (e: Exception) {
            log.error("courseId: {$courseId}")
            throw CourseException.COURSE_NOT_DELETED.courseException
        }
    }

    override fun readAll(): List<CourseDTO> {
        val courseList = courseRepo.findAll()
        return courseList.map { CourseDTO(it) }
    }

    override fun getCoursesByNickname(nickname: String): List<CourseDTO> {
        val byMemberNickname = courseRepo.getByMemberNickname(nickname)
        return byMemberNickname.map { CourseDTO(it) } ?: emptyList() // 빈 리스트 반환
    }

    //수강중인 강의 목록
    override fun getMemberCoursesByMemberId(memberId: Long): List<MemberCourseDTO> {
        val memberCourseList = memberCourseRepo.getMemberCourse(memberId)
        return memberCourseRepo.getMemberCourse(memberId).ifEmpty {
            throw CourseException.MEMBER_COURSE_NOT_FOUND.courseException
        }.map { MemberCourseDTO(it) }.also {
            log.info("memberCourses: $it")
        }
    }

    override fun getCoursesByMemberId(memberId: Long): List<CourseDTO> {
        return memberCourseRepo.getMemberCourseByMemberMemberId(memberId).ifEmpty {
            throw CourseException.MEMBER_COURSE_NOT_FOUND.courseException
        }.mapNotNull { it.course }.map{ CourseDTO(it) }.also { log.info("courses: $it") }
    }

    // 강사 닉네임 반환
//    fun getInstructorNicknameByCourseId(courseId: Long): String {
//        val course = courseRepository.findById(courseId)
//            .orElseThrow { IllegalArgumentException("해당 코스를 찾을 수 없습니다.") }
//
//        return course.member.nickname
//    }
}
