package com.ItsTime.ItNovation.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private String authNum;


    @Value("${spring.mail.username}")
    private String from;

    public void createCode() { // 인증번호 무작위 6자리 -> 문자로 들어가게 됨.
        Random random = new Random();

        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(9);
            key.append(Integer.toString(index));
            authNum = key.toString();
        }
        System.out.println(authNum);
    }

    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException{
        createCode();
        String toEmail = email;
        String title = "MOVIELOG 비밀번호 교체 관련 인증 번호";

        MimeMessage message = javaMailSender.createMimeMessage(); //MimeMessage 객체를 직접 생성하여 메일을 발송하는 방법
        message.addRecipients(RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(from);
        message.setText(setContext(authNum), "utf-8", "html");
        System.out.println(email);
        return message;
    }


    public String sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException{

        MimeMessage emailForm = createEmailForm(toEmail);

        javaMailSender.send(emailForm);
        return authNum;
    }


    //타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context(); // thymeleaf 사용을 위해 사용
        context.setVariable("code", code);
        return templateEngine.process("mailTest", context); //mail.html
    }


}