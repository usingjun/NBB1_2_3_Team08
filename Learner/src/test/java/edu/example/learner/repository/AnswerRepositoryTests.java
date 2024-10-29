package edu.example.learner.repository;

import edu.example.learner.member.entity.Member;
import edu.example.learner.member.repository.MemberRepository;
import edu.example.learner.qna.answer.entity.Answer;
import edu.example.learner.qna.answer.repository.AnswerRepository;
import edu.example.learner.qna.inquiry.entity.Inquiry;
import edu.example.learner.qna.inquiry.repository.InquiryRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerRepositoryTests {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(
                Member.builder()
                        .email("aaa@aaa.com")
                        .password("aaa")
                        .nickname("aaa")
                        .build()
        );

        inquiryRepository.save(
                Inquiry.builder()
                        .inquiryTitle("inquiry title")
                        .inquiryContent("inquiry content")
                        .member(member)
                        .build()
        );
    }

    @Test
    @Order(1)
    void testInsert() {
        //GIVEN
        inquiryRepository.save(Inquiry.builder().inquiryId(1L).member(Member.builder().memberId(1L).build()).build());
        Answer answer = Answer.builder()
                .answerContent("answer test content")
                .inquiry(Inquiry.builder().inquiryId(1L).build())
                .build();

        //WHEN
        Answer savedAnswer = answerRepository.save(answer);

        //THEN
        assertNotNull(savedAnswer);
        assertEquals(1L, savedAnswer.getAnswerId());
        assertEquals("answer test content", savedAnswer.getAnswerContent());

        log.info("--- savedAnswer : " + savedAnswer);
    }

    @Test
    @Transactional
    @Order(2)
    void testRead() {
        //GIVEN
        Long answerId = 1L;

        //WHEN
        Optional<Answer> foundAnswer = answerRepository.findById(answerId);

        //THEN
        assertNotNull(foundAnswer);
        assertEquals(answerId, foundAnswer.get().getAnswerId());

        log.info("--- foundAnswer : " + foundAnswer);
    }

    @Test
    @Transactional
    @Order(3)
    void testUpdate() {
        //GIVEN
        Long answerId = 1L;
        Answer answer = answerRepository.findById(answerId).get();

        //WHEN
        answer.changeAnswerContent("new answer content");
        Answer updatedAnswer = answerRepository.findById(answerId).get();

        //THEN
        assertEquals("new answer content", answer.getAnswerContent());

        log.info("--- updatedAnswer : " + updatedAnswer);
    }

    @Test
    @Order(4)
    void testDelete() {
        //GIVEN
        Long answerId = 1L;
        assertTrue(answerRepository.existsById(answerId));

        //WHEN
        answerRepository.deleteById(answerId);

        //THEN
        assertFalse(answerRepository.findById(answerId).isPresent());
    }
}
