package edu.example.learner.courseabout.video.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVideo is a Querydsl query type for Video
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVideo extends EntityPathBase<Video> {

    private static final long serialVersionUID = 1844641304L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVideo video = new QVideo("video");

    public final edu.example.learner.courseabout.course.entity.QCourse course;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> currentVideoTime = createNumber("currentVideoTime", Long.class);

    public final StringPath description = createString("description");

    public final StringPath title = createString("title");

    public final NumberPath<Long> totalVideoDuration = createNumber("totalVideoDuration", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath url = createString("url");

    public final NumberPath<Long> video_Id = createNumber("video_Id", Long.class);

    public QVideo(String variable) {
        this(Video.class, forVariable(variable), INITS);
    }

    public QVideo(Path<? extends Video> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVideo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVideo(PathMetadata metadata, PathInits inits) {
        this(Video.class, metadata, inits);
    }

    public QVideo(Class<? extends Video> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new edu.example.learner.courseabout.course.entity.QCourse(forProperty("course"), inits.get("course")) : null;
    }

}

