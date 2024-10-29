package edu.example.learner.qna.faq.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFAQ is a Querydsl query type for FAQ
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFAQ extends EntityPathBase<FAQ> {

    private static final long serialVersionUID = -530554650L;

    public static final QFAQ fAQ = new QFAQ("fAQ");

    public final StringPath faqCategory = createString("faqCategory");

    public final StringPath faqContent = createString("faqContent");

    public final DateTimePath<java.time.LocalDateTime> faqCreateDate = createDateTime("faqCreateDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> faqId = createNumber("faqId", Long.class);

    public final StringPath faqTitle = createString("faqTitle");

    public final DateTimePath<java.time.LocalDateTime> faqUpdateDate = createDateTime("faqUpdateDate", java.time.LocalDateTime.class);

    public QFAQ(String variable) {
        super(FAQ.class, forVariable(variable));
    }

    public QFAQ(Path<? extends FAQ> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFAQ(PathMetadata metadata) {
        super(FAQ.class, metadata);
    }

}

