package edu.example.learner.qna.faq.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "FAQ")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    private String faqTitle;

    private String faqContent;

    @CreatedDate
    private LocalDateTime faqCreateDate;

    @LastModifiedDate
    private LocalDateTime faqUpdateDate;

    private String faqCategory;

    public void changeFaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;
    }

    public void changeFaqContent(String faqContent) {
        this.faqContent = faqContent;
    }

    public void changeFaqCategory(String faqCategory) {
        this.faqCategory = faqCategory;
    }
}
