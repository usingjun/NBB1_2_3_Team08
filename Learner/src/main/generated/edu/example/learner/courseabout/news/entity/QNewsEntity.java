package edu.example.learner.courseabout.news.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNewsEntity is a Querydsl query type for NewsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNewsEntity extends EntityPathBase<NewsEntity> {

    private static final long serialVersionUID = 2100268987L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNewsEntity newsEntity = new QNewsEntity("newsEntity");

    public final edu.example.learner.courseabout.course.entity.QCourse courseNews;

    public final ListPath<HeartNews, QHeartNews> heartNewsList = this.<HeartNews, QHeartNews>createList("heartNewsList", HeartNews.class, QHeartNews.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> newsDate = createDateTime("newsDate", java.time.LocalDateTime.class);

    public final StringPath newsDescription = createString("newsDescription");

    public final NumberPath<Long> newsId = createNumber("newsId", Long.class);

    public final StringPath newsName = createString("newsName");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QNewsEntity(String variable) {
        this(NewsEntity.class, forVariable(variable), INITS);
    }

    public QNewsEntity(Path<? extends NewsEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNewsEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNewsEntity(PathMetadata metadata, PathInits inits) {
        this(NewsEntity.class, metadata, inits);
    }

    public QNewsEntity(Class<? extends NewsEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.courseNews = inits.isInitialized("courseNews") ? new edu.example.learner.courseabout.course.entity.QCourse(forProperty("courseNews"), inits.get("courseNews")) : null;
    }

}

