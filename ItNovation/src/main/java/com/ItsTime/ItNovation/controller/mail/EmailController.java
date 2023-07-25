package com.ItsTime.ItNovation.controller.mail;

import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.mail.dto.CodeCheckRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.PasswordFindRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.RewritePasswordRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.mail.EmailService;
import com.ItsTime.ItNovation.service.user.UserService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private Map<String, String> authCodeMap = new ConcurrentHashMap<>(); // 동시성 문제가 있을거 같아 ConcurrentHashMap 사용해서 멀티 스레드에서 무사히 돌아갈 수 있도록 진행..

    @PostMapping("/passwordfind/emailSend")
    public ResponseEntity<String> mailConfirm(@RequestBody PasswordFindRequestDto req){
        try {

            log.info(req.getEmail());
            User findUser = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new IllegalStateException(GeneralErrorCode.UNKNOWN_USER.getMessage()));
            log.info(findUser.getEmail());
            String authCode = emailService.sendMail(findUser.getEmail());
            authCodeMap.put(req.getEmail(), authCode);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/passwordfind/finalCheck")
    public ResponseEntity<String> checkCode(@RequestBody CodeCheckRequestDto checkRequestDto){
        String sendedCode = checkRequestDto.getCode();
        String email = checkRequestDto.getEmail();
        if(emailService.isCodeSame(sendedCode, email, authCodeMap)){
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }



    @PutMapping("/passwordfind/rewritePw")
    public ResponseEntity<String> rewritePassword(@RequestBody RewritePasswordRequestDto rewritePasswordRequestDto){
        String email = rewritePasswordRequestDto.getEmail();
        String updatePassword = rewritePasswordRequestDto.getPassword();

        return userService.updatePassword(email, updatePassword);
    }

}
