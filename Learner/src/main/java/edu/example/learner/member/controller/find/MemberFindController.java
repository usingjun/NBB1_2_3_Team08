package edu.example.learner.member.controller.find;

import edu.example.learner.member.dto.ResetPasswordRequest;
import edu.example.learner.member.dto.SendResetPasswordEmailReq;
import edu.example.learner.member.dto.SendResetPasswordEmailRes;
import edu.example.learner.member.service.find.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/find")
@Tag(name = "회원 찾기", description = "회원을 찾는 API")
public class MemberFindController {
    private final LoginService loginService;

    // 전화번호로 이메일(아이디) 찾기
    @GetMapping("/emails")
    @Operation(summary = "전화번호로 이메일(아이디) 찾기", description = "전화번호로 가입된 회원의 이메일(아이디)을 찾습니다.")
    public ResponseEntity<List<String>> findEmailByPhoneNumber(@RequestParam String phoneNumber) {

        List<String> emails = loginService.findEmailsByPhoneNumber(phoneNumber);
        if (emails.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 응답
        }
        return ResponseEntity.ok(emails); // 200 응답
    }

    @PostMapping("/send-reset-password")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호를 찾기 위한 이메일을 전송합니다.")
    public SendResetPasswordEmailRes sendResetPassword(
            @Validated @RequestBody SendResetPasswordEmailReq req) {

        loginService.checkMemberByEmail(req.getEmail());
        String uuid = loginService.sendResetPasswordEmail(req.getEmail());
        return SendResetPasswordEmailRes.builder()
                .uuid(uuid)
                .build();
    }

    @PostMapping("/reset-password/{uuid}")
    @Operation(summary = "비밀번호 재설정", description = "UUID로 비밀번호를 재설정합니다.")
    public ResponseEntity<?> resetPassword(
            @PathVariable String uuid,
            @Validated @RequestBody ResetPasswordRequest req) {

        loginService.resetPassword(uuid, req.getNewPassword());
        return ResponseEntity.ok().body("비밀번호가 성공적으로 재설정되었습니다.");
    }

}
// 테스트
//    @PostMapping("/emails")
//    public String mail(@RequestBody MailDTO data) {
//        String res = loginService.sendSimpleMessage(data);
//        return res;
//    }


