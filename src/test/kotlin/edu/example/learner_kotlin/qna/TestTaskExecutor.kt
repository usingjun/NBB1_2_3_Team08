package edu.example.learner_kotlin.qna

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.qna.answer.entity.Answer
import edu.example.learner_kotlin.qna.answer.repository.AnswerRepository
import edu.example.learner_kotlin.qna.faq.entity.FAQ
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import edu.example.learner_kotlin.qna.faq.repository.FAQRepository
import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import edu.example.learner_kotlin.qna.inquiry.entity.InquiryStatus
import edu.example.learner_kotlin.qna.inquiry.repository.InquiryRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestTaskExecutor(
    val memberRepository: MemberRepository,
    val inquiryRepository: InquiryRepository,
    val faqRepository: FAQRepository,
    val answerRepository: AnswerRepository
) :
    ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val member = Member().apply {
            email = "test@example.org"
            nickname = "test"
            role = Role.USER
        }
        memberRepository.save(member)

        for (i in 1..10L) {
            val inquiry = Inquiry().apply {
                inquiryTitle = "test $i"
                inquiryContent = "test"
                inquiryStatus = InquiryStatus.CONFIRMING
                this.member = Member().apply {
                    memberId = 1L
                }
            }
            inquiryRepository.save(inquiry)

            val answer = Answer().apply {
                answerContent = "test $i"
                this.inquiry = Inquiry().apply {
                    inquiryId = i
                }
            }
            answerRepository.save(answer)
        }
        val inquiry = Inquiry().apply {
            inquiryTitle = "test for answer"
            inquiryContent = "test"
            inquiryStatus = InquiryStatus.CONFIRMING
            this.member = Member().apply {
                memberId = 1L
            }
        }
        inquiryRepository.save(inquiry)

        for (i in 1..10) {
            val faq = FAQ().apply {
                faqTitle = "test course $i"
                faqContent = "test course"
                faqCategory = FAQCategory.COURSE

                faqRepository.save(this)
            }
        }
        for (i in 1..10) {
            val faq = FAQ().apply {
                faqTitle = "test $i login"
                faqContent = "test login"
                faqCategory = FAQCategory.LOGIN

                faqRepository.save(this)
            }
        }
    }
}