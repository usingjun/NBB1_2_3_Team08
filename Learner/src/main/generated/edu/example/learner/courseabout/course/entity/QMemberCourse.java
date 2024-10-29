package edu.example.learner.courseabout.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCourse is a Querydsl query type for MemberCourse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCourse extends EntityPathBase<MemberCourse> {

    private static final long serialVersionUID = -1686991550L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCourse memberCourse1 = new QMemberCourse("memberCourse1");

    public final QCourse course;

    public final edu.example.learner.member.entity.QMember member;

    public final NumberPath<Long> memberCourse = createNumber("memberCourse", Long.class);

    public final DateTimePath<java.util.Date> purchaseDate = createDateTime("purchaseDate", java.util.Date.class);

    public QMemberCourse(String variable) {
        this(MemberCourse.class, forVariable(variable), INITS);
    }

    public QMemberCourse(Path<? extends MemberCourse> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCourse(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCourse(PathMetadata metadata, PathInits inits) {
        this(MemberCourse.class, metadata, inits);
    }

    public QMemberCourse(Class<? extends MemberCourse> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course"), inits.get("course")) : null;
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

