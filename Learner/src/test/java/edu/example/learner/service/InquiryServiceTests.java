package edu.example.learner.service;

import edu.example.learner.qna.inquiry.dto.InquiryDTO;
import edu.example.learner.qna.inquiry.entity.InquiryStatus;
import edu.example.learner.member.entity.Member;
import edu.example.learner.qna.inquiry.repository.InquiryRepository;
import edu.example.learner.qna.inquiry.service.InquiryService;
import edu.example.learner.member.repository.MemberRepository;
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
public class InquiryServiceTests {
    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private InquiryRepository inquiryRepository;

    @Test
    @Order(1)
    void testRegister() {
        IntStream.rangeClosed(1,10).forEach(i -> {
            //GIVEN
            InquiryDTO inquiryDTO = InquiryDTO.builder()
                    .inquiryTitle("register test " + i)
                    .inquiryContent("content" + i)
                    .memberId(1L)
                    .build();

            //WHEN
            InquiryDTO registeredInquiryDTO = inquiryService.register(inquiryDTO);

            //THEN
            assertNotNull(registeredInquiryDTO);
            assertEquals(i, registeredInquiryDTO.getInquiryId());

            log.info("--- registeredInquiryDTO: " + registeredInquiryDTO);
        });
    }

    @Test
    @Order(2)
    void testRead() {
        //GIVEN
        Long inquiryId = 1L;

        //WHEN
        InquiryDTO foundInquiryDTO = inquiryService.read(inquiryId);

        //THEN
        assertNotNull(foundInquiryDTO);
        assertEquals(inquiryId, foundInquiryDTO.getInquiryId());

        log.info("--- foundInquiryDTO: " + foundInquiryDTO);
    }

    @Test
    @Order(3)
    void testReadAll() {
        //GIVEN

        //WHEN
        List<InquiryDTO> inquiryDTOList = inquiryService.readAll();

        //THEN
        assertNotNull(inquiryDTOList);
        assertEquals(10, inquiryDTOList.size());

        inquiryDTOList.forEach(System.out::println);
    }

    @Test
    @Order(4)
    void testUpdate() {
        //GIVEN
        InquiryDTO inquiryDTO = InquiryDTO.builder()
                .inquiryId(10L)
                .inquiryTitle("new title")
                .inquiryContent("new content")
                .inquiryStatus(InquiryStatus.RESOLVED.name())
                .build();

        //WHEN
        InquiryDTO updatedInquiryDTO = inquiryService.update(inquiryDTO);

        //THEN
        assertNotNull(updatedInquiryDTO);
        assertEquals("new title", updatedInquiryDTO.getInquiryTitle());
        assertEquals("new content", updatedInquiryDTO.getInquiryContent());

        log.info("--- updatedInquiryDTO: " + updatedInquiryDTO);
    }

    @Test
    @Order(5)
    void testDelete() {
        //GIVEN
        Long inquiryId = 9L;

        //WHEN
        inquiryService.delete(inquiryId);

        //THEN
        assertFalse(inquiryRepository.existsById(inquiryId));
    }
}
