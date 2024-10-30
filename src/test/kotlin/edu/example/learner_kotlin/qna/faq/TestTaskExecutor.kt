package edu.example.learner_kotlin.qna.faq

import edu.example.learner_kotlin.qna.faq.entity.FAQ
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import edu.example.learner_kotlin.qna.faq.repository.FAQRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestTaskExecutor(val faqRepository: FAQRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        for (i in 1..10) {
            val faq = FAQ().apply {
                faqTitle = "test course"
                faqContent = "test course"
                faqCategory = FAQCategory.COURSE

                faqRepository.save(this)
            }
        }
        for (i in 1..10) {
            val faq = FAQ().apply {
                faqTitle = "test login"
                faqContent = "test login"
                faqCategory = FAQCategory.LOGIN

                faqRepository.save(this)
            }
        }
    }
}