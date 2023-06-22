package com.ItsTime.ItNovation.controller.mail;

import com.ItsTime.ItNovation.domain.mail.EmailAuthRequestDto;
import com.ItsTime.ItNovation.service.mail.EmailService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/test/mailConfirm")
    public String mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException
    {
        String authCode = emailService.sendMail(emailDto.getEmail());
        return authCode;
    }


    //Invalid server address
    //받는사람의 도메인이 존재하지 않거나, 상대방 메일서버의 DNS 설정상의 문제가 있어 메일을 보낼 수 없는 상황입니다.
    //주로 사용자들이 받는사람의 도메인을 잘못 적어서 발생한 경우가 많습니다.

















}
