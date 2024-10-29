package edu.example.learner.courseabout.courseqna.service;

import edu.example.learner.courseabout.courseqna.dto.CourseAnswerDTO;
import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.courseqna.repository.CourseInquiryRepository;
import edu.example.learner.courseabout.exception.CourseAnswerException;
import edu.example.learner.courseabout.courseqna.repository.CourseAnswerRepository;
import edu.example.learner.courseabout.exception.CourseInquiryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CourseAnswerService {
    private final CourseAnswerRepository courseAnswerRepository;
    private final CourseInquiryRepository courseInquiryRepository;

    //강의 답변 등록
    public CourseAnswerDTO register(CourseAnswerDTO courseAnswerDTO){
        try{
            CourseInquiry courseInquiry = courseInquiryRepository.findById(courseAnswerDTO.getInquiryId())
                    .orElseThrow(CourseInquiryException.NOT_FOUND::get);

            CourseAnswer courseAnswer = courseAnswerDTO.toEntity();
            courseAnswer.setCourseInquiry(courseInquiry);

            courseAnswerRepository.save(courseAnswer);
            return new CourseAnswerDTO(courseAnswer);
        } catch (Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseAnswerException.NOT_REGISTERED.get();
        }
    }


    //특정 강의 문의의 전체 답변 보기
    public List<CourseAnswerDTO> readAll(Long inquiryId){
        try{
            List<CourseAnswer> courseAnswer = courseAnswerRepository.getCourseAnswers(inquiryId).get();
            List<CourseAnswerDTO> courseAnswerDTOList = new ArrayList<>();

            if(courseAnswer == null || courseAnswer.size() == 0){
                throw  CourseAnswerException.NOT_FOUND.get();
            }

            for(CourseAnswer courseAnswerDTO : courseAnswer){
                courseAnswerDTOList.add(new CourseAnswerDTO(courseAnswerDTO));
            }
            log.info("CourseAnswerDTOList: " + courseAnswerDTOList);
            return courseAnswerDTOList;
        }catch (Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseAnswerException.NOT_REGISTERED.get();
        }
    }


    //강의 답변 수정
    public CourseAnswerDTO update(CourseAnswerDTO courseAnswerDTO){
        Optional<CourseAnswer> foundCourseAnswer = courseAnswerRepository.findById(courseAnswerDTO.getAnswerId());
        CourseAnswer modifyCourseAnswer = foundCourseAnswer.orElseThrow(CourseAnswerException.NOT_FOUND::get);

        try{
            modifyCourseAnswer.changeAnswerContent(courseAnswerDTO.getAnswerContent());
            return new CourseAnswerDTO(modifyCourseAnswer);
        }catch (Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseAnswerException.NOT_REGISTERED.get();
        }
    }


    //강의 답변 삭제
    public void delete(Long answerId){
        Optional<CourseAnswer> foundCourseAnswer = courseAnswerRepository.findById(answerId);
        CourseAnswer courseAnswer = foundCourseAnswer.orElseThrow(CourseAnswerException.NOT_FOUND::get);

        try{
            courseAnswerRepository.delete(courseAnswer);
        }catch (Exception e){
            log.error("--- "+ e.getMessage());
            throw CourseAnswerException.NOT_REMOVED.get();
        }
    }
}
