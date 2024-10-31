package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.service

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.dto.CourseInquiryDTO
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.exception.CourseInquiryException
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository.CourseInquiryRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CourseInquiryService(
    private val courseInquiryRepository: CourseInquiryRepository,
    private val modelMapper: ModelMapper
) {

    // 강의 문의 등록
    fun register(courseInquiryDTO: CourseInquiryDTO): CourseInquiryDTO {
        val courseInquiry = courseInquiryDTO.toEntity()
        courseInquiryRepository.save(courseInquiry)
        return modelMapper.map(courseInquiry, CourseInquiryDTO::class.java)
    }

    // 강의 문의 리스트 조회
    fun readAll(courseId: Long): List<CourseInquiryDTO> {
        val courseInquiries = courseInquiryRepository.getCourseInquirys(courseId) ?: emptyList()
        return courseInquiries.map { modelMapper.map(it, CourseInquiryDTO::class.java) }
    }

    // 강의 문의 조회
    fun read(inquiryId: Long): CourseInquiryDTO {
        val courseInquiry = courseInquiryRepository.findById(inquiryId)
            .orElseThrow { CourseInquiryException.NOT_FOUND.courseInquiryTaskException }
        return modelMapper.map(courseInquiry, CourseInquiryDTO::class.java)
    }

    // 강의 문의 수정 - 제목과 내용만 수정 가능
    fun update(courseInquiryDTO: CourseInquiryDTO): CourseInquiryDTO {
        val modifyCourseInquiry = courseInquiryRepository.findById(courseInquiryDTO.inquiryId!!)
            .orElseThrow { CourseInquiryException.NOT_FOUND.courseInquiryTaskException }

        modifyCourseInquiry.changeInquiryTitle(courseInquiryDTO.inquiryTitle!!)
        modifyCourseInquiry.changeInquiryContent(courseInquiryDTO.inquiryContent!!)

        return modelMapper.map(modifyCourseInquiry, CourseInquiryDTO::class.java)
    }

    // 강의 문의 상태 변경 - 사용자 변경 불가
    fun updateStatus(courseInquiryDTO: CourseInquiryDTO): CourseInquiryDTO {
        val modifyCourseInquiry = courseInquiryRepository.findById(courseInquiryDTO.inquiryId!!)
            .orElseThrow { CourseInquiryException.NOT_FOUND.courseInquiryTaskException }

        modifyCourseInquiry.changeInquiryStatus(courseInquiryDTO.inquiryStatus!!)

        return modelMapper.map(modifyCourseInquiry, CourseInquiryDTO::class.java)
    }

    // 강의 문의 삭제
    fun delete(inquiryId: Long) {
        val courseInquiry = courseInquiryRepository.findById(inquiryId)
            .orElseThrow { CourseInquiryException.NOT_FOUND.courseInquiryTaskException }

        courseInquiryRepository.delete(courseInquiry)
    }
}