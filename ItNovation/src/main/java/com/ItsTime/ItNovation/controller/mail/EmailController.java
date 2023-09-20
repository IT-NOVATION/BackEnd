package com.ItsTime.ItNovation.controller.mail;

import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.NotFoundException;
import com.ItsTime.ItNovation.domain.mail.dto.CodeCheckRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.PasswordFindRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.RewritePasswordRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.mail.EmailService;
import com.ItsTime.ItNovation.service.user.UserService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email/password-find")
@Tag(name="비밀번호 찾기 메일 관련 API")
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private Map<String, String> authCodeMap = new ConcurrentHashMap<>(); // 동시성 문제가 있을거 같아 ConcurrentHashMap 사용해서 멀티 스레드에서 무사히 돌아갈 수 있도록 진행..

    @PostMapping("/email-send")
    @Operation(summary = "메일 보내기")
    public ResponseEntity<String> mailSend(@RequestBody PasswordFindRequestDto req){
        try {
            User findUser = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            String authCode = emailService.sendMail(findUser.getEmail());
            authCodeMap.put(req.getEmail(), authCode);
        }catch (MessagingException | UnsupportedEncodingException e){
            return new ResponseEntity<>(ErrorCode.SEND_MAIL_ERROR.getHttpStatus());
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/final-check")
    @Operation(summary = "입력된 인증번호와 비교하기")
    public ResponseEntity<String> checkCode(@RequestBody CodeCheckRequestDto checkRequestDto){
        String sendedCode = checkRequestDto.getCode();
        String email = checkRequestDto.getEmail();
        if(emailService.isCodeSame(sendedCode, email, authCodeMap)){
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(ErrorCode.INCORRECT_EMAIL_CODE.getHttpStatus());
    }



    @PostMapping("/rewrite-pw")
    @Operation(summary = "재발급 받은 패스워드로 사용자 정보 업데이트하기")
    public ResponseEntity<String> rewritePassword(@RequestBody RewritePasswordRequestDto rewritePasswordRequestDto){
        String email = rewritePasswordRequestDto.getEmail();
        String updatePassword = rewritePasswordRequestDto.getPassword();
        return userService.updatePassword(email, updatePassword);
    }

}
