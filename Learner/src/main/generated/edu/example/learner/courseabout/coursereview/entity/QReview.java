package edu.example.learner.courseabout.coursereview.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -442773411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final edu.example.learner.courseabout.course.entity.QCourse course;

    public final edu.example.learner.member.entity.QMember member;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> reviewCreatedDate = createDateTime("reviewCreatedDate", java.time.LocalDateTime.class);

    public final StringPath reviewDetail = createString("reviewDetail");

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final StringPath reviewName = createString("reviewName");

    public final EnumPath<ReviewType> reviewType = createEnum("reviewType", ReviewType.class);

    public final DateTimePath<java.time.LocalDateTime> reviewUpdatedDate = createDateTime("reviewUpdatedDate", java.time.LocalDateTime.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new edu.example.learner.courseabout.course.entity.QCourse(forProperty("course"), inits.get("course")) : null;
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

