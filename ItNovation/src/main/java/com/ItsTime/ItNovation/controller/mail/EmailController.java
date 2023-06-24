package com.ItsTime.ItNovation.controller.mail;

import com.ItsTime.ItNovation.domain.mail.dto.CodeCheckRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.PasswordFindRequestDto;
import com.ItsTime.ItNovation.service.mail.EmailService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private Map<String, String> authCodeMap = new ConcurrentHashMap<>(); // 동시성 문제가 있을거 같아 ConcurrentHashMap 사용해서 멀티 스레드에서 무사히 돌아갈 수 있도록 진행..

    @PostMapping("/passwordfind/emailSend")
    public HttpStatus mailConfirm(@RequestBody PasswordFindRequestDto emailDto){
        try {
            String authCode = emailService.sendMail(emailDto.getEmail());
            String email = emailDto.getEmail();
            authCodeMap.put(email, authCode);
        }catch (Exception e){
           return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @PostMapping("/passwordfind/finalCheck")
    public HttpStatus checkCode(@RequestBody CodeCheckRequestDto checkRequestDto){
        String sendedCode = checkRequestDto.getCode();
        String email = checkRequestDto.getEmail();

        if(emailService.isCodeSame(sendedCode, email, authCodeMap)){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
