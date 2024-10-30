package edu.example.learner.courseabout.coursereview.repository

import edu.example.learner.courseabout.coursereview.entity.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReviewRepository : JpaRepository<Review?, Long?> {
    @Query(
        ("select r from Review r" +
                " join fetch r.course" +
                " where r.course.courseId=:courseId" +
                " order by r.reviewId desc")
    )
    fun getCourseReview(@Param("courseId") courseId: Long?): Optional<List<Review?>?>

    @Query(
        ("select r from Review r " +
                " join fetch r.course" +
                " where r.course.member.nickname=:nickname" +
                " order by r.reviewId desc")
    )
    fun getInstructorReview(@Param("nickname") nickname: String?): Optional<List<Review?>?>
}
