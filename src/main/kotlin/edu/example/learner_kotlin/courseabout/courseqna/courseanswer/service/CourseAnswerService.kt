package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.service

import edu.example.learner_kotlin.courseabout.courseqna.courseanswer.exception.CourseAnswerException
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.dto.CourseAnswerDTO
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseAnswer
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.exception.CourseInquiryException
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository.CourseAnswerRepository
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository.CourseInquiryRepository
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.exception.MemberException
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CourseAnswerService(
    private val courseAnswerRepository: CourseAnswerRepository,
    private val courseInquiryRepository: CourseInquiryRepository,
    //private val memberRepository: MemberRepository,
    private val modelMapper: ModelMapper
) {

    // 강의 답변 등록
    fun register(courseAnswerDTO: CourseAnswerDTO): CourseAnswerDTO {
        return try {
            val courseInquiry = courseInquiryRepository.findById(courseAnswerDTO.inquiryId!!)
                .orElseThrow { CourseInquiryException.NOT_FOUND.courseInquiryTaskException }

            // memberId로 Member 조회
//            val member = memberRepository.findById(courseAnswerDTO.memberId!!)
//                .orElseThrow { MemberException.MEMBER_NOT_FOUND.memberTaskException }

            // CourseAnswer 생성 시 Member와 CourseInquiry 설정
            val courseAnswer = courseAnswerDTO.toEntity().apply {
                this.courseInquiry = courseInquiry
            }

            courseAnswerRepository.save(courseAnswer)
            modelMapper.map(courseAnswer, CourseAnswerDTO::class.java)
        } catch (e: Exception) {
            log.error("--- ${e.message}")
            throw CourseAnswerException.NOT_REGISTERED.courseAnswerTaskException
        }
    }

    // 특정 강의 문의의 전체 답변 보기
    fun readAll(inquiryId: Long): List<CourseAnswerDTO> {
        val courseAnswers = courseAnswerRepository.getCourseAnswers(inquiryId) ?: emptyList()

        // CourseAnswer를 CourseAnswerDTO로 매핑
//        val courseAnswerDTOList: List<CourseAnswerDTO> = courseAnswers.map {
//            modelMapper.map(it, CourseAnswerDTO::class.java)
//        }

        // modelMapper 대신 CourseAnswerDTO의 보조 생성자 사용
        val courseAnswerDTOList = courseAnswers.map { CourseAnswerDTO(it) }

        // 각 DTO의 profileImage가 포함되어 있는지 확인
        courseAnswerDTOList.forEach { dto ->
            log.info("CourseAnswerDTO!!!!!! : answerId=${dto.answerId}, profileImage=${dto.profileImage != null}")
        }

        log.info("CourseAnswerDTOList: $courseAnswerDTOList")
        return courseAnswerDTOList
    }


    // 강의 답변 수정
    fun update(courseAnswerDTO: CourseAnswerDTO): CourseAnswerDTO {
        val modifyCourseAnswer = courseAnswerRepository.findById(courseAnswerDTO.answerId!!)
            .orElseThrow { CourseAnswerException.NOT_FOUND.courseAnswerTaskException }

        return try {
            modifyCourseAnswer.changeAnswerContent(courseAnswerDTO.answerContent!!)
            modelMapper.map(modifyCourseAnswer, CourseAnswerDTO::class.java)
        } catch (e: Exception) {
            log.error("--- ${e.message}")
            throw CourseAnswerException.NOT_REGISTERED.courseAnswerTaskException
        }
    }

    // 강의 답변 삭제
    fun delete(answerId: Long) {
        val courseAnswer = courseAnswerRepository.findById(answerId)
            .orElseThrow { CourseAnswerException.NOT_FOUND.courseAnswerTaskException }

        try {
            courseAnswerRepository.delete(courseAnswer)
        } catch (e: Exception) {
            log.error("--- ${e.message}")
            throw CourseAnswerException.NOT_REMOVED.courseAnswerTaskException
        }
    }
}