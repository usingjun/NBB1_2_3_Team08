package edu.example.learner.courseabout.courseqna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseAnswer is a Querydsl query type for CourseAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseAnswer extends EntityPathBase<CourseAnswer> {

    private static final long serialVersionUID = -217702806L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseAnswer courseAnswer = new QCourseAnswer("courseAnswer");

    public final StringPath answerContent = createString("answerContent");

    public final DateTimePath<java.time.LocalDateTime> answerCreateDate = createDateTime("answerCreateDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final QCourseInquiry courseInquiry;

    public final edu.example.learner.member.entity.QMember member;

    public QCourseAnswer(String variable) {
        this(CourseAnswer.class, forVariable(variable), INITS);
    }

    public QCourseAnswer(Path<? extends CourseAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseAnswer(PathMetadata metadata, PathInits inits) {
        this(CourseAnswer.class, metadata, inits);
    }

    public QCourseAnswer(Class<? extends CourseAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.courseInquiry = inits.isInitialized("courseInquiry") ? new QCourseInquiry(forProperty("courseInquiry"), inits.get("courseInquiry")) : null;
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

