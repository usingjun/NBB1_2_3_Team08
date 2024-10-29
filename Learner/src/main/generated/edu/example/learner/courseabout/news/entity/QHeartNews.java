package edu.example.learner.courseabout.news.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHeartNews is a Querydsl query type for HeartNews
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHeartNews extends EntityPathBase<HeartNews> {

    private static final long serialVersionUID = 1515160692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHeartNews heartNews = new QHeartNews("heartNews");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final edu.example.learner.member.entity.QMember member;

    public final QNewsEntity newsEntity;

    public QHeartNews(String variable) {
        this(HeartNews.class, forVariable(variable), INITS);
    }

    public QHeartNews(Path<? extends HeartNews> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHeartNews(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHeartNews(PathMetadata metadata, PathInits inits) {
        this(HeartNews.class, metadata, inits);
    }

    public QHeartNews(Class<? extends HeartNews> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
        this.newsEntity = inits.isInitialized("newsEntity") ? new QNewsEntity(forProperty("newsEntity"), inits.get("newsEntity")) : null;
    }

}

