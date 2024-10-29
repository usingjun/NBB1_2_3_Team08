package edu.example.learner.qna.faq.service;

import edu.example.learner.qna.faq.dto.FAQDTO;

import java.util.List;

public interface FAQService {
    FAQDTO read(Long faqId);

    List<FAQDTO> readAll();

    List<FAQDTO> readByCategory(String faqCategory);

    FAQDTO register(FAQDTO faqDTO);

    FAQDTO update(FAQDTO faqDTO);

    void delete(Long faqId);
}
