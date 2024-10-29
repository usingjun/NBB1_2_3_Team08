package edu.example.learner.courseabout.repository;

import edu.example.learner.courseabout.course.dto.CourseDTO;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.exception.CourseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@SpringBootTest
@RequiredArgsConstructor
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepository;

    // 테스트 데이터 생성
    @BeforeEach
    void setUp() {
        courseRepository.save(Course.builder()
                .courseAttribute(CourseAttribute.JAVA)
                .courseDescription("Java 강의")
                .courseName("Java Basics")
                .courseLevel(1)
                .coursePrice(3000L)
                .sale(false)
                .build());

        courseRepository.save(Course.builder()
                .courseAttribute(CourseAttribute.PYHTON)
                .courseDescription("Python 강의")
                .courseName("Python Basics")
                .courseLevel(1)
                .coursePrice(4000L)
                .sale(false)
                .build());
    }

    @Test
    void add() {
        Course course = courseRepository.save(Course.builder()
                .courseAttribute(CourseAttribute.C)
                .courseDescription("testDB3")
                .courseName("test강의3")
                .courseLevel(2)
                .coursePrice(5000L)
                .sale(false)
                .build());
        assertThat(course).isNotNull();
    }

    @Test
    void readByCourseAttribute() {
        List<Course> courseList = courseRepository.readByCourseAttribute(CourseAttribute.JAVA);
        assertThat(courseList).isNotNull();
        assertThat(courseList.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void updateCourse() {
        // 존재하는 Course ID를 사용
        CourseDTO courseDTO = CourseDTO.builder()
                .courseId(1L) // Assuming the first course saved has ID 1
                .courseDescription("변경")
                .courseName("modName")
                .courseAttribute(CourseAttribute.JAVA.name())
                .coursePrice(3000L)
                .sale(false)
                .courseLevel(3)
                .build();

        Optional<Course> optionalCourse = courseRepository.findById(courseDTO.getCourseId());
        optionalCourse.ifPresent(course -> {
            course.changeCourseLevel(courseDTO.getCourseLevel());
            course.changeCourseDescription(courseDTO.getCourseDescription());
            course.changeCourseStatus(CourseAttribute.valueOf(courseDTO.getCourseAttribute()));
            course.changeSale(courseDTO.isSale());
        });
        Course updatedCourse = courseRepository.findById(courseDTO.getCourseId()).orElseThrow(CourseException.COURSE_NOT_FOUND::get);
        assertThat(updatedCourse.getCourseDescription()).isEqualTo("변경");
    }

    @Test
    void deleteCourse() {
        // Assuming the first course saved has ID 1
        courseRepository.deleteById(2L);
        Optional<Course> deletedCourse = courseRepository.findById(2L);
        assertThat(deletedCourse).isEmpty();
    }

    @Test
    void readAll() {
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).isNotNull();
        assertThat(courseList.size()).isGreaterThan(1); // 2개의 초기 데이터가 있으므로
    }
}
