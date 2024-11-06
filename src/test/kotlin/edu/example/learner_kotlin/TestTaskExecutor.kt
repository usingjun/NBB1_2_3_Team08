package edu.example.learner_kotlin

import edu.example.learner_kotlin.attendance.entity.Attendance
import edu.example.learner_kotlin.attendance.repository.AttendanceRepository
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.video.entity.Video
import edu.example.learner_kotlin.courseabout.video.repository.VideoRepository
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.member_video.entity.MemberVideo
import edu.example.learner_kotlin.member_video.repository.MemberVideoRepository
import edu.example.learner_kotlin.qna.answer.entity.Answer
import edu.example.learner_kotlin.qna.answer.repository.AnswerRepository
import edu.example.learner_kotlin.qna.faq.entity.FAQ
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import edu.example.learner_kotlin.qna.faq.repository.FAQRepository
import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import edu.example.learner_kotlin.qna.inquiry.entity.InquiryStatus
import edu.example.learner_kotlin.qna.inquiry.repository.InquiryRepository
import edu.example.learner_kotlin.studytable.entity.StudyTable
import edu.example.learner_kotlin.studytable.repository.StudyTableRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestTaskExecutor(
    val memberRepository: MemberRepository,
    val inquiryRepository: InquiryRepository,
    val faqRepository: FAQRepository,
    val answerRepository: AnswerRepository,
    val studyTableRepository: StudyTableRepository,
    val attendanceRepository: AttendanceRepository,
    val memberVideoRepository : MemberVideoRepository,
    val videoRepository: VideoRepository,
    val courseRepository: CourseRepository,
) :
    ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val member1 = Member().apply {
            email = "test1@example.org"
            nickname = "test1"
            role = Role.USER
        }
        memberRepository.save(member1)
        val member2 = Member().apply {
            email = "test2@example.org"
            nickname = "test2"
            role = Role.ADMIN
        }
        memberRepository.save(member2)

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

        val studyTable = StudyTable().apply {
            studyTime = 30
            completed = 1
            this.member = Member().apply { memberId = 1L }
        }
        studyTableRepository.save(studyTable)

        val attendance = Attendance().apply {
            this.member = Member().apply { memberId = 1L }
        }
        attendanceRepository.save(attendance)

        val course = Course().apply {
            this.courseName = "test"
            this.courseDescription = "test"
            this.member = Member().apply { memberId = 3L}
            this.coursePrice = 10000L
            this.courseLevel = 1
        }
        courseRepository.save(course)

        for (i in 1..2L) {
            val video = Video().apply {
                this.course = Course().apply {this.courseId = 1L}
            }
            videoRepository.save(video)
        }

        val memberVideo1 = MemberVideo().apply {
            this.studyTime = 20
            this.member = Member().apply { memberId = 1L }
            this.video = Video().apply { videoId = 1L }
        }
        memberVideoRepository.save(memberVideo1)
        val memberVideo2 = MemberVideo().apply {
            this.studyTime = 30
            this.watched = true
            this.member = Member().apply { memberId = 1L }
            this.video = Video().apply { videoId = 2L }
        }
        memberVideoRepository.save(memberVideo2)
        val memberVideo3 = MemberVideo().apply {
            this.studyTime = 40
            this.watched = true
            this.member = Member().apply { memberId = 2L }
            this.video = Video().apply { videoId = 1L }
        }
        memberVideoRepository.save(memberVideo3)
    }
}