package edu.example.learner.repository;

import edu.example.learner.qna.faq.entity.FAQ;
import edu.example.learner.qna.faq.entity.FAQCategory;
import edu.example.learner.qna.faq.repository.FAQRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FAQRepositoryTests {
    @Autowired
    private FAQRepository faqRepository;

    @Test
    @Order(1)
    void testInsert() {
        //GIVEN
        FAQ faq = FAQ.builder()
                .faqTitle("faq test title")
                .faqContent("faq test content")
                .faqCategory(FAQCategory.COURSE.name())
                .build();

        //WHEN
        FAQ savedFAQ = faqRepository.save(faq);

        //THEN
        assertNotNull(savedFAQ);
        assertEquals(1L, savedFAQ.getFaqId());
        assertEquals("faq test title", savedFAQ.getFaqTitle());
        assertEquals("faq test content", savedFAQ.getFaqContent());
        assertEquals(FAQCategory.COURSE.name(), savedFAQ.getFaqCategory());

        log.info("--- savedFAQ : " + savedFAQ);
    }

    @Test
    @Order(2)
    void testRead() {
        //GIVEN
        Long faqId = 1L;

        //WHEN
        Optional<FAQ> foundFAQ = faqRepository.findById(faqId);

        //THEN
        assertNotNull(foundFAQ);
        assertEquals(faqId, foundFAQ.get().getFaqId());

        log.info("--- foundFAQ : " + foundFAQ);
    }

    @Test
    @Order(4)
    void testFindByCategory() {
        //GIVEN
        String faqCategory = FAQCategory.COURSE.name();

        //WHEN
        List<FAQ> faqList = faqRepository.findByFaqCategory(faqCategory);

        //THEN
        assertNotNull(faqList);
        faqList.forEach(faq -> assertEquals(faqCategory, faq.getFaqCategory()));

        log.info("--- faqList : " + faqList);
    }

    @Test
    @Transactional
    @Order(3)
    void testUpdate() {

        //GIVEN
        Long faqId = 1L;
        FAQ faq = faqRepository.findById(faqId).get();

        //WHEN
        faq.changeFaqTitle("new faq title");
        faq.changeFaqContent("new faq content");
        faq.changeFaqCategory(FAQCategory.OTHERS.name());
        FAQ updatedFAQ = faqRepository.findById(faqId).get();

        //THEN
        assertNotNull(updatedFAQ);
        assertEquals("new faq title", updatedFAQ.getFaqTitle());
        assertEquals("new faq content", updatedFAQ.getFaqContent());
        assertEquals(FAQCategory.OTHERS.name(), updatedFAQ.getFaqCategory());

        log.info("--- updatedFAQ : " + updatedFAQ);
    }

    @Test
    @Order(5)
    void testDelete() {
        //GIVEN
        Long faqId = 1L;
        assertTrue(faqRepository.existsById(faqId));

        //WHEN
        faqRepository.deleteById(faqId);

        //THEN
        assertFalse(faqRepository.findById(faqId).isPresent());
    }
}
