package edu.example.learner.alarm.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmDTO is a Querydsl query type for AlarmDTO
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAlarmDTO extends BeanPath<AlarmDTO> {

    private static final long serialVersionUID = 362266867L;

    public static final QAlarmDTO alarmDTO = new QAlarmDTO("alarmDTO");

    public final StringPath alarmContent = createString("alarmContent");

    public final NumberPath<Long> alarmId = createNumber("alarmId", Long.class);

    public final BooleanPath alarmStatus = createBoolean("alarmStatus");

    public final StringPath alarmTitle = createString("alarmTitle");

    public final StringPath alarmType = createString("alarmType");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath priority = createString("priority");

    public QAlarmDTO(String variable) {
        super(AlarmDTO.class, forVariable(variable));
    }

    public QAlarmDTO(Path<? extends AlarmDTO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmDTO(PathMetadata metadata) {
        super(AlarmDTO.class, metadata);
    }

}

