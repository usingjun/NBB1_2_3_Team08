package edu.example.learner.courseabout.coursereview.repository;

import edu.example.learner.courseabout.coursereview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r" +
            " join fetch r.course" +
            " where r.course.courseId=:courseId" +
            " order by r.reviewId desc")
    Optional<List<Review>> getCourseReview(@Param("courseId") Long courseId);

    @Query("select r from Review r " +
            " join fetch r.course" +
            " where r.course.member.nickname=:nickname" +
            " order by r.reviewId desc")
    Optional<List<Review>> getInstructorReview(@Param("nickname") String nickname);
}
