package edu.example.learner.qna.faq.service;

import edu.example.learner.qna.faq.repository.FAQRepository;
import edu.example.learner.member.exception.LearnerException;
import edu.example.learner.qna.faq.dto.FAQDTO;
import edu.example.learner.qna.faq.entity.FAQ;
import edu.example.learner.qna.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class FAQServiceImpl implements FAQService {
    private final FAQRepository faqRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public FAQDTO read(Long faqId) {
        return new FAQDTO(faqRepository.findById(faqId).orElseThrow(LearnerException.NOT_FOUND_EXCEPTION::getTaskException));
    }

    @Override
    public List<FAQDTO> readByCategory(String faqCategory) {
        return faqRepository.findByFaqCategory(faqCategory).stream().map(FAQDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<FAQDTO> readAll() {
        return faqRepository.findAll().stream().map(FAQDTO::new).collect(Collectors.toList());
    }

    @Override
    public FAQDTO register(FAQDTO faqDTO) {
        try {
            FAQ savedFAQ = faqRepository.save(faqDTO.toEntity());
            return new FAQDTO(savedFAQ);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw LearnerException.NOT_REGISTERED_EXCEPTION.getTaskException();
        }
    }

    @Override
    public FAQDTO update(FAQDTO faqDTO) {
        FAQ faq = faqRepository.findById(faqDTO.getFaqId()).orElseThrow();
        try {
            faq.changeFaqTitle(faqDTO.getFaqTitle());
            faq.changeFaqContent(faqDTO.getFaqContent());
            faq.changeFaqCategory(faqDTO.getFaqCategory());
            return new FAQDTO(faq);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw LearnerException.NOT_MODIFIED_EXCEPTION.getTaskException();
        }
    }

    @Override
    public void delete(Long faqId) {
        faqRepository.findById(faqId).orElseThrow();
        try {
            faqRepository.deleteById(faqId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw LearnerException.NOT_REMOVED_EXCEPTION.getTaskException();
        }
    }
}
