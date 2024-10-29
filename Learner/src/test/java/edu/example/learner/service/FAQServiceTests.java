package edu.example.learner.service;

import edu.example.learner.qna.faq.dto.FAQDTO;
import edu.example.learner.qna.faq.entity.FAQCategory;
import edu.example.learner.qna.faq.repository.FAQRepository;
import edu.example.learner.qna.faq.service.FAQService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FAQServiceTests {
    @Autowired
    private FAQService faqService;
    @Autowired
    private FAQRepository faqRepository;
    
    @Test
    @Order(1)
    void testRegister() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            //GIVEN
            FAQDTO faqDTO1 = FAQDTO.builder()
                    .faqTitle("title " + i)
                    .faqContent("content" + i)
                    .faqCategory(FAQCategory.COURSE.name())
                    .build();
            FAQDTO faqDTO2 = FAQDTO.builder()
                    .faqTitle("title " + i)
                    .faqContent("content" + i)
                    .faqCategory(FAQCategory.OTHERS.name())
                    .build();

            //WHEN
            FAQDTO registeredFAQDTO1 = faqService.register(faqDTO1);
            FAQDTO registeredFAQDTO2 = faqService.register(faqDTO2);

            //THEN
            assertNotNull(registeredFAQDTO1);
            assertEquals(2L * i - 1, registeredFAQDTO1.getFaqId());

            log.info("--- registeredFAQDTO1: " + registeredFAQDTO1);
            log.info("--- registeredFAQDTO2: " + registeredFAQDTO2);
        });
    }

    @Test
    @Order(2)
    void testRead() {
        //GIVEN
        Long faqId = 1L;

        //WHEN
        FAQDTO foundFAQDTO = faqService.read(faqId);

        //THEN
        assertNotNull(foundFAQDTO);
        assertEquals(faqId, foundFAQDTO.getFaqId());

        log.info("--- foundFAQDTO: " + foundFAQDTO);
    }

    @Test
    @Order(3)
    void testReadAll() {
        //GIVEN

        //WHEN
        List<FAQDTO> faqDTOList = faqService.readAll();

        //THEN
        assertNotNull(faqDTOList);
        assertEquals(10, faqDTOList.size());

        log.info("--- faqDTOList: " + faqDTOList);
    }

    @Test
    @Order(6)
    void testReadByCategory() {
        //GIVEN
        FAQCategory faqCategory = FAQCategory.OTHERS;

        //WHEN
        List<FAQDTO> faqDTOList = faqService.readByCategory(faqCategory.name());

        //THEN
        assertNotNull(faqDTOList);

        log.info("--- faqDTOList: " + faqDTOList);
    }

    @Test
    @Order(4)
    void testUpdate() {
        //GIVEN
        FAQDTO faqDTO = FAQDTO.builder()
                .faqId(10L)
                .faqTitle("new title")
                .faqContent("new content")
                .faqCategory(FAQCategory.OTHERS.name())
                .build();

        //WHEN
        FAQDTO updatedFAQDTO = faqService.update(faqDTO);

        //THEN
        assertNotNull(updatedFAQDTO);
        assertEquals("new title", updatedFAQDTO.getFaqTitle());
        assertEquals("new content", updatedFAQDTO.getFaqContent());
        assertEquals(FAQCategory.OTHERS.name(), updatedFAQDTO.getFaqCategory());

        log.info("--- updatedFAQDTO: " + updatedFAQDTO);
    }

    @Test
    @Order(5)
    void testDelete() {
        //GIVEN
        Long faqId = 9L;

        //WHEN
        faqService.delete(faqId);

        //THEN
        assertFalse(faqRepository.existsById(faqId));
    }
}
