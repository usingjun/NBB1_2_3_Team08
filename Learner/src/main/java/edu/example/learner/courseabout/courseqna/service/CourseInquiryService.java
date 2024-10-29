package edu.example.learner.courseabout.courseqna.service;

import edu.example.learner.courseabout.courseqna.dto.CourseInquiryDTO;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.exception.CourseInquiryException;
import edu.example.learner.courseabout.courseqna.repository.CourseInquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CourseInquiryService {
    private final CourseInquiryRepository courseInquiryRepository;

    //강의 문의 등록
    public CourseInquiryDTO register(CourseInquiryDTO courseInquiryDTO){
        try{
            CourseInquiry courseInquiry = courseInquiryDTO.toEntity();
            courseInquiryRepository.save(courseInquiry);
            return new CourseInquiryDTO(courseInquiry);
        } catch (Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseInquiryException.NOT_REGISTERED.get();
        }
    }

    //강의 문의 리스트 조회
    public List<CourseInquiry> readAll(Long courseId){
        try{
            return courseInquiryRepository.getCourseInquirys(courseId).orElse(null);
        } catch (Exception e){
            log.error("--- " + e.getMessage());
            throw CourseInquiryException.NOT_FETCHED.get();
        }
    }

    //강의 문의 조회
    public CourseInquiryDTO read(Long inquiryId){
        Optional<CourseInquiry> foundcourseInquiry = courseInquiryRepository.findById(inquiryId);
        CourseInquiry courseInquiry = foundcourseInquiry.orElseThrow(CourseInquiryException.NOT_FOUND::get);
        return new CourseInquiryDTO(courseInquiry);
    }


    //강의 문의 수정 - 제목과 내용만 수정 가능
    public CourseInquiryDTO update(CourseInquiryDTO courseInquiryDTO){
        Optional<CourseInquiry> foundCourseInquiry = courseInquiryRepository.findById(courseInquiryDTO.getInquiryId());
        CourseInquiry modifyCourseInquiry = foundCourseInquiry.orElseThrow(CourseInquiryException.NOT_FOUND::get);

        try{
            modifyCourseInquiry.changeInqueryTitle(courseInquiryDTO.getInquiryTitle());
            modifyCourseInquiry.changeInquiryContent(courseInquiryDTO.getInquiryContent());
            return new CourseInquiryDTO(modifyCourseInquiry);
        } catch(Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseInquiryException.NOT_MODIFIED.get();
        }
    }

    //강의 문의 상태 변경 - 사용자 변경 불가
    public CourseInquiryDTO updateStatus(CourseInquiryDTO courseInquiryDTO){
        Optional<CourseInquiry> foundCourseInquiry = courseInquiryRepository.findById(courseInquiryDTO.getInquiryId());
        CourseInquiry modifyCourseInquiry = foundCourseInquiry.orElseThrow(CourseInquiryException.NOT_FOUND::get);

        try{
            modifyCourseInquiry.changeInquiryStatus(courseInquiryDTO.getInquiryStatus());
            return new CourseInquiryDTO(modifyCourseInquiry);
        } catch(Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseInquiryException.NOT_MODIFIED.get();
        }
    }


    //강의 문의 삭제
    public void delete(Long inquiryId){
        Optional<CourseInquiry> foundCourseInquiry = courseInquiryRepository.findById(inquiryId);
        CourseInquiry courseInquiry = foundCourseInquiry.orElseThrow(CourseInquiryException.NOT_FOUND::get);

        try{
            courseInquiryRepository.delete(courseInquiry);
        }catch (Exception e){
            log.error("--- " + e.getMessage());
            throw CourseInquiryException.NOT_REMOVED.get();
        }
    }
}
