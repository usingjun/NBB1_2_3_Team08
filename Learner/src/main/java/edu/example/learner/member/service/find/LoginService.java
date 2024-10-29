package edu.example.learner.member.service.find;

import edu.example.learner.member.dto.ResetPasswordRequest;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.repository.MemberRepository;
import edu.example.learner.redis.RedisServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final RedisServiceImpl redisService;
    private final MemberRepository memberRepository;
    private final JavaMailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${props.reset-password-url}")
    private String resetPwUrl;

    // 전화번호로 이메일(아이디) 찾기
    public List<String> findEmailsByPhoneNumber(String phoneNumber) {
        List<Member> membersByPhoneNumber = memberRepository.findByPhoneNumber(phoneNumber);
        return membersByPhoneNumber.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList());
    }

    public String makeUuid() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public String sendResetPasswordEmail(String email) {
        String uuid = makeUuid();
        String title = "요청하신 비밀번호 재설정입니다"; // 이메일 제목
        String content = "비밀번호를 재설정하려면 아래 링크를 클릭하세요.(24시간유지)\n"
                + "<a href=\"" + resetPwUrl + "/" + uuid + "\">"
                + resetPwUrl + "/" + uuid + "</a>"; // 이메일 내용
        mailSend(email, title, content);
        saveUuidAndEmail(uuid, email);
        return uuid;
    }

    public void mailSend(String toMail, String title, String content) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");// 보내는 사람
            helper.setFrom(new InternetAddress(fromEmail, "Learner"));
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveUuidAndEmail(String uuid, String email) {
        redisService.setValues(uuid, email, Duration.ofHours(24));
    }

    @Transactional(readOnly = true)
    public void checkMemberByEmail(String email) {
        boolean exists = memberRepository.existsByEmail(email);
        if (!exists) {
            throw new RuntimeException("해당 이메일을 사용하는 회원이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void resetPassword(String uuid, String newPassword) {
        String email = redisService.getValue(uuid);
        if (email == null) {
            throw new RuntimeException("유효하지 않은 요청입니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        member.changePassword(passwordEncoder.encode((newPassword)));

        redisService.deleteValue(uuid);
    }

    // 메일보내기 테스트
//    public String sendSimpleMessage(MailDTO data) {
//
//        // MailDTO 객체의 데이터를 출력합니다.
//        System.out.println(data);
//
//        // SimpleMailMessage 객체를 생성하고 이메일 정보를 설정합니다.
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(data.getEmail()); // 발신자 이메일 설정
//        message.setTo("qhrn123@gmail.com"); // 수신자 이메일 설정
//        message.setSubject(data.getName()); // 이메일 제목 설정
//        message.setText(data.getMessage() + data.getEmail()); // 이메일 본문 설정
//        emailSender.send(message); // 이메일 전송
//
//        // 전송 성공 로그를 출력합니다.
//        log.info("성공 메세지 {} : ", message);
//        return "전송 성공!";
//    }



}
