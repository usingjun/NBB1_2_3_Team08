package edu.example.learner.qna.faq.dto;

import edu.example.learner.qna.faq.entity.FAQ;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FAQDTO {
    private Long faqId;

    @NotEmpty
    private String faqTitle;

    @NotEmpty
    private String faqContent;

    private LocalDateTime faqCreateDate;

    private LocalDateTime faqUpdateDate;

    @NotEmpty
    private String faqCategory;

    public FAQDTO(FAQ faq) {
        this.faqId = faq.getFaqId();
        this.faqTitle = faq.getFaqTitle();
        this.faqContent = faq.getFaqContent();
        this.faqCreateDate = faq.getFaqCreateDate();
        this.faqUpdateDate = faq.getFaqUpdateDate();
        this.faqCategory = faq.getFaqCategory();
    }

    public FAQ toEntity() {
        return FAQ.builder()
                .faqId(faqId)
                .faqTitle(faqTitle)
                .faqContent(faqContent)
                .faqCreateDate(faqCreateDate)
                .faqUpdateDate(faqUpdateDate)
                .faqCategory(faqCategory)
                .build();
    }
}
