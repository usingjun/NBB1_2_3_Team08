package edu.example.learner.courseabout.order.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderItemDTO is a Querydsl query type for OrderItemDTO
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QOrderItemDTO extends BeanPath<OrderItemDTO> {

    private static final long serialVersionUID = 1065538434L;

    public static final QOrderItemDTO orderItemDTO = new QOrderItemDTO("orderItemDTO");

    public final StringPath courseAttribute = createString("courseAttribute");

    public final NumberPath<Long> courseId = createNumber("courseId", Long.class);

    public final StringPath courseName = createString("courseName");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public QOrderItemDTO(String variable) {
        super(OrderItemDTO.class, forVariable(variable));
    }

    public QOrderItemDTO(Path<? extends OrderItemDTO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderItemDTO(PathMetadata metadata) {
        super(OrderItemDTO.class, metadata);
    }

}

