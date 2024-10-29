package edu.example.learner.studytable.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyTable is a Querydsl query type for StudyTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyTable extends EntityPathBase<StudyTable> {

    private static final long serialVersionUID = -1300701024L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyTable studyTable = new QStudyTable("studyTable");

    public final NumberPath<Integer> completed = createNumber("completed", Integer.class);

    public final edu.example.learner.member.entity.QMember member;

    public final DatePath<java.time.LocalDate> studyDate = createDate("studyDate", java.time.LocalDate.class);

    public final NumberPath<Long> studyTableId = createNumber("studyTableId", Long.class);

    public final NumberPath<Integer> studyTime = createNumber("studyTime", Integer.class);

    public QStudyTable(String variable) {
        this(StudyTable.class, forVariable(variable), INITS);
    }

    public QStudyTable(Path<? extends StudyTable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyTable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyTable(PathMetadata metadata, PathInits inits) {
        this(StudyTable.class, metadata, inits);
    }

    public QStudyTable(Class<? extends StudyTable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

