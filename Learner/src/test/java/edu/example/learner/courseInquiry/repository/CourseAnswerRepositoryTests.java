package edu.example.learner.courseInquiry.repository;

import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.courseqna.repository.CourseAnswerRepository;
import edu.example.learner.courseabout.courseqna.service.CourseAnswerService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class CourseAnswerRepositoryTests {
    @Autowired
    private CourseAnswerService courseAnswerService;
    @Autowired
    private CourseAnswerRepository courseAnswerRepository;

    @Test
    @DisplayName("문의 답변 등록 테스트")
    public void testRegister(){
        CourseAnswer courseAnswer = CourseAnswer.builder()
                .answerContent("Test Course Inquiry Answer2")
                .courseInquiry(CourseInquiry.builder().inquiryId(1L).build())
                .build();

        CourseAnswer savedAnswer = courseAnswerRepository.save(courseAnswer);

        assertThat(savedAnswer).isNotNull();
        assertThat(savedAnswer.getAnswerId()).isNotNull();
        assertThat(savedAnswer.getAnswerContent()).isEqualTo(courseAnswer.getAnswerContent());
    }

    @Test
    @DisplayName("문의 답변 조회 테스트")
    public void testReadAll(){
        Long inquiryId = 1L;
        Optional<List<CourseAnswer>> answers = courseAnswerRepository.getCourseAnswers(inquiryId);
        assertThat(answers).isNotEmpty();
    }

    @Test
    @DisplayName("문의 답변 수정 테스트")
    public void testUpdate(){
        Long answerId = 1L;
        String answerContent = "변경된 내용!";

        Optional<CourseAnswer> foundCourseAnswer = courseAnswerRepository.findById(answerId);
        CourseAnswer updateCourseAnswer = foundCourseAnswer.get();
        updateCourseAnswer.changeAnswerContent(answerContent);

        courseAnswerRepository.save(updateCourseAnswer);

        foundCourseAnswer = courseAnswerRepository.findById(answerId);
        assertThat(foundCourseAnswer.get().getAnswerContent()).isEqualTo(answerContent);
    }

    @Test
    @DisplayName("문의 답변 삭제 테스트")
    public void testDelete(){
        Long answerId = 3L;

        courseAnswerRepository.deleteById(answerId);

        Optional<CourseAnswer> deleteCourseAnswer = courseAnswerRepository.findById(answerId);
        assertThat(deleteCourseAnswer).isEmpty();
    }
}

