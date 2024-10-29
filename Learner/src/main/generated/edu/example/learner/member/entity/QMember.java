package edu.example.learner.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1581299734L;

    public static final QMember member = new QMember("member1");

    public final ListPath<edu.example.learner.courseabout.courseqna.entity.CourseAnswer, edu.example.learner.courseabout.courseqna.entity.QCourseAnswer> courseAnswers = this.<edu.example.learner.courseabout.courseqna.entity.CourseAnswer, edu.example.learner.courseabout.courseqna.entity.QCourseAnswer>createList("courseAnswers", edu.example.learner.courseabout.courseqna.entity.CourseAnswer.class, edu.example.learner.courseabout.courseqna.entity.QCourseAnswer.class, PathInits.DIRECT2);

    public final ListPath<edu.example.learner.courseabout.courseqna.entity.CourseInquiry, edu.example.learner.courseabout.courseqna.entity.QCourseInquiry> courseInquiries = this.<edu.example.learner.courseabout.courseqna.entity.CourseInquiry, edu.example.learner.courseabout.courseqna.entity.QCourseInquiry>createList("courseInquiries", edu.example.learner.courseabout.courseqna.entity.CourseInquiry.class, edu.example.learner.courseabout.courseqna.entity.QCourseInquiry.class, PathInits.DIRECT2);

    public final ListPath<edu.example.learner.courseabout.course.entity.Course, edu.example.learner.courseabout.course.entity.QCourse> courses = this.<edu.example.learner.courseabout.course.entity.Course, edu.example.learner.courseabout.course.entity.QCourse>createList("courses", edu.example.learner.courseabout.course.entity.Course.class, edu.example.learner.courseabout.course.entity.QCourse.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final ListPath<edu.example.learner.courseabout.news.entity.HeartNews, edu.example.learner.courseabout.news.entity.QHeartNews> heartNewsList = this.<edu.example.learner.courseabout.news.entity.HeartNews, edu.example.learner.courseabout.news.entity.QHeartNews>createList("heartNewsList", edu.example.learner.courseabout.news.entity.HeartNews.class, edu.example.learner.courseabout.news.entity.QHeartNews.class, PathInits.DIRECT2);

    public final StringPath imageType = createString("imageType");

    public final ListPath<edu.example.learner.qna.inquiry.entity.Inquiry, edu.example.learner.qna.inquiry.entity.QInquiry> inquiries = this.<edu.example.learner.qna.inquiry.entity.Inquiry, edu.example.learner.qna.inquiry.entity.QInquiry>createList("inquiries", edu.example.learner.qna.inquiry.entity.Inquiry.class, edu.example.learner.qna.inquiry.entity.QInquiry.class, PathInits.DIRECT2);

    public final StringPath introduction = createString("introduction");

    public final ListPath<edu.example.learner.courseabout.course.entity.MemberCourse, edu.example.learner.courseabout.course.entity.QMemberCourse> memberCourses = this.<edu.example.learner.courseabout.course.entity.MemberCourse, edu.example.learner.courseabout.course.entity.QMemberCourse>createList("memberCourses", edu.example.learner.courseabout.course.entity.MemberCourse.class, edu.example.learner.courseabout.course.entity.QMemberCourse.class, PathInits.DIRECT2);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ArrayPath<byte[], Byte> profileImage = createArray("profileImage", byte[].class);

    public final ListPath<edu.example.learner.courseabout.coursereview.entity.Review, edu.example.learner.courseabout.coursereview.entity.QReview> reviews = this.<edu.example.learner.courseabout.coursereview.entity.Review, edu.example.learner.courseabout.coursereview.entity.QReview>createList("reviews", edu.example.learner.courseabout.coursereview.entity.Review.class, edu.example.learner.courseabout.coursereview.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

