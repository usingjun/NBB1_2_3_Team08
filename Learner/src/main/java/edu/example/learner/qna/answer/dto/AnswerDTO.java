package edu.example.learner.qna.answer.dto;

import edu.example.learner.qna.answer.entity.Answer;
import edu.example.learner.qna.inquiry.entity.Inquiry;
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
public class AnswerDTO {
    private Long answerId;

    @NotEmpty
    private String answerContent;

    private LocalDateTime answerCreateDate;

    private Long inquiryId;

    public AnswerDTO(Answer answer) {
        this.answerId = answer.getAnswerId();
        this.answerContent = answer.getAnswerContent();
        this.answerCreateDate = answer.getAnswerCreateDate();
        this.inquiryId = answer.getInquiry().getInquiryId();
    }

    public Answer toEntity() {
        return Answer.builder()
                .answerId(answerId)
                .answerContent(answerContent)
                .answerCreateDate(answerCreateDate)
                .inquiry(Inquiry.builder().inquiryId(inquiryId).build())
                .build();
    }
}
