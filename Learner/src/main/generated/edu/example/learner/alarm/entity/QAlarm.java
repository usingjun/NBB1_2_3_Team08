package edu.example.learner.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = -1728755652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final StringPath alarmContent = createString("alarmContent");

    public final NumberPath<Long> alarmId = createNumber("alarmId", Long.class);

    public final BooleanPath alarmStatus = createBoolean("alarmStatus");

    public final StringPath alarmTitle = createString("alarmTitle");

    public final EnumPath<AlarmType> alarmType = createEnum("alarmType", AlarmType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final edu.example.learner.member.entity.QMember member;

    public final EnumPath<Priority> priority = createEnum("priority", Priority.class);

    public QAlarm(String variable) {
        this(Alarm.class, forVariable(variable), INITS);
    }

    public QAlarm(Path<? extends Alarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarm(PathMetadata metadata, PathInits inits) {
        this(Alarm.class, metadata, inits);
    }

    public QAlarm(Class<? extends Alarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new edu.example.learner.member.entity.QMember(forProperty("member")) : null;
    }

}

