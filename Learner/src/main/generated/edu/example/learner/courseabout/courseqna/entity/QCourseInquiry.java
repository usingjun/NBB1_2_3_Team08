package edu.example.learner.courseabout.courseqna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseInquiry is a Querydsl query type for CourseInquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseInquiry extends EntityPathBase<CourseInquiry> {

    private static final long serialVersionUID = 349339803L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseInquiry courseInquiry = new QCourseInquiry("courseInquiry");

    public final ListPath<CourseAnswer, QCourseAnswer> answers = this.<CourseAnswer, QCourseAnswer>createList("answers", CourseAnswer.class, QCourseAnswer.class, PathInits.DIRECT2);

    public final edu.example.learner.courseabout.course.entity.QCourse course;

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final StringPath inquiryContent = createString("inquiryContent");

    public final NumberPath<Long> inquiryGood = createNumber("inquiryGood", Long.class);

    public final NumberPath<Long> inquiryId = createNumber("inquiryId", Long.class);

    public final EnumPath<InquiryStatus> inquiryStatus = createEnum("inquiryStatus", InquiryStatus.class);

    public final StringPath inquiryTitle = createString("inquiryTitle");

    public final edu.example.learner.member.entity.QMember member;

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public QCourseInquiry(String variable) {
        this(CourseInquiry.class, forVariable(variable), INITS);
    }

    public QCourseInquiry(Path<? extends CourseInquiry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseInquiry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseInquiry(PathMetadata metadata, PathInits inits) {
        this(CourseInquiry.class, metadata, inits);
    }

    public QCourseInquiry(Class<? extends CourseInquiry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new edu.example.learner.courseabout.course.entity.QCourse(forProperty("course"), inits.get("course")) : null;
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

