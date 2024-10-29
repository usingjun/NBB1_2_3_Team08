package edu.example.learner;
//
//기본 MEMBER 3명 삽입

import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import edu.example.learner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class MemberInitializer implements CommandLineRunner {
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 초기 데이터 삽입
//        insertMembers();
//    }
//
//    private void insertMembers() {
//        // 예시로 추가할 사용자 데이터
//        String[][] members = {
//                {"ADMIN@gmail.com", "자기소개란 입니다.", "관리자", "010-0000-0000", "ADMIN"},
//                {"customer@gmail.com", "자기소개란 입니다.", "학생", "010-0000-0000", "USER"},
//                {"instructor@gmail.com", "자기소개란 입니다.", "김영한", "010-0000-0000", "INSTRUCTOR"},
//        };
//
//        for (String[] memberData : members) {
//            Member member = new Member();
//            member.setEmail(memberData[0]);
//            member.setIntroduction(memberData[1]);
//            member.setNickname(memberData[2]);
//            member.setPhoneNumber(memberData[3]);
//            member.setRole(Role.valueOf(memberData[4]));
//            member.setPassword(passwordEncoder.encode("1234")); // 비밀번호 암호화
//            memberRepository.save(member);
//        }
//    }
//}