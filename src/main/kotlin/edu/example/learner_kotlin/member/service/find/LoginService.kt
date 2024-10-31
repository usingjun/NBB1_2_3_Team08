package edu.example.learner_kotlin.member.service.find

import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.redis.RedisServiceImpl
import io.jsonwebtoken.security.Keys.password
import jakarta.mail.MessagingException
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.UnsupportedEncodingException
import java.time.Duration
import java.util.*

@Service
@Transactional
class LoginService(
    private val redisService: RedisServiceImpl,
    private val memberRepository: MemberRepository,
    private val emailSender: JavaMailSender,
    private val passwordEncoder: PasswordEncoder
) {
    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    @Value("\${props.reset-password-url}")
    private lateinit var resetPwUrl: String


    // 전화번호로 이메일(아이디) 찾기
    fun findEmailsByPhoneNumber(phoneNumber: String): List<String> {
        return memberRepository.findByPhoneNumber(phoneNumber)
            .filterNotNull()  // null인 Member 객체 필터링
            .mapNotNull { it.email }  // null인 email 필터링
    }

    fun makeUuid(): String = UUID.randomUUID().toString()


    fun sendResetPasswordEmail(email: String): String {
        val uuid = makeUuid()
        val title = "요청하신 비밀번호 재설정입니다"
        val content = """
            비밀번호를 재설정하려면 아래 링크를 클릭하세요.(24시간유지)
            <a href="$resetPwUrl/$uuid">$resetPwUrl/$uuid</a>
        """.trimIndent()

        mailSend(email, title, content)
        saveUuidAndEmail(uuid, email)
        return uuid
    }

    fun mailSend(toMail: String, title: String, content: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        try {
            MimeMessageHelper(message, true, "UTF-8").apply {
                setFrom(InternetAddress(fromEmail, "Learner"))
                setTo(toMail)
                setSubject(title)
                setText(content, true)
            }
            emailSender.send(message)
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }
    }


    fun saveUuidAndEmail(uuid: String, email: String) {
        redisService.setValues(uuid, email, Duration.ofHours(24))
    }

    @Transactional(readOnly = true)
    fun checkMemberByEmail(email: String) {
        if (!memberRepository.existsByEmail(email)) {
            throw RuntimeException("해당 이메일을 사용하는 회원이 존재하지 않습니다.")
        }
    }

    fun resetPassword(uuid: String, newPassword: String) {
        val email = redisService.getValue(uuid)

        val member = memberRepository.findByEmail(email)

        member.apply {
            password = passwordEncoder.encode(newPassword)
        }
        redisService.deleteValue(uuid)
    }
}