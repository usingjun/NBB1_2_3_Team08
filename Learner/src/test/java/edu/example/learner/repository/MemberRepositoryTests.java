package edu.example.learner.repository;

import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import lombok.extern.log4j.Log4j2;
import edu.example.learner.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("맴버추가테스트")
    @Order(1)
    public void testInsertMember() {
        IntStream.rangeClosed(1,5).forEach(i -> {
            Member member = Member.builder()
                                  .email( i + "@gmail.com")
                                  .password("PasswordEncoder")
                                  .nickname("Nickname" + i)
                                  .phoneNumber("010-0000-000" + i)
                                  .introduction("안녕하세요")
                                  .role(i > 2 ? Role.ADMIN : Role.USER)
                                  .build();

        Member savedmember = memberRepository.save(member);

        assertNotNull(savedmember);
        assertEquals(i, savedmember.getMemberId());
        assertNotNull(savedmember.getCreateDate());
        });
    }

    @Test
    @DisplayName("멤버조회테스트")
    @Order(2)
    public void testReadMember(){
        Long memberId = 1L;

        Member foundMember = memberRepository.getMemberInfo(memberId);

        assertNotNull(foundMember);
        log.info(foundMember);

        assertEquals(memberId, foundMember.getMemberId());
    }

    @Test
    @DisplayName("멤버수정테스트")
    @Order(3)
    public void testUpdateMember() {
        Long memberId = 3L;
        String email = "변경 주소";
        String password = "변경 비밀번호";
        String nickname = "변경 닉네임";
        String introduction = "변경 자기소개";

        Member member = memberRepository.getMemberInfo(memberId);
        assertNotNull(member);

        member.changeEmail(email);
        member.changePassword(password);
        member.changeNickname(nickname);
        member.changeIntroduction(introduction);

        memberRepository.save(member);

        member = memberRepository.getMemberInfo(memberId);
        assertEquals(email, member.getEmail());
        assertEquals(password, member.getPassword());
        assertEquals(nickname, member.getNickname());
        assertEquals(introduction, member.getIntroduction());
    }

    @Test
    @DisplayName("멤버삭제테스트")
    @Order(4)
    public void testDeleteMember() {
        long memberId = 1L;

        assertTrue(memberRepository.existsById((long) memberId));

        memberRepository.deleteById((long) Math.toIntExact(memberId));
        assertFalse(memberRepository.existsById((long) memberId));
    }

    @Test
    @DisplayName("멤버총조회")
    @Order(5)
    public void testFindMember() {
        Iterable<Member> members = memberRepository.getAllMembers();

        for (Member member : members) {
            log.info(member);
        }
    }
}
