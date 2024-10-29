package edu.example.learner.courseabout.video.repository;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCourse_CourseId(Long courseId);
}
