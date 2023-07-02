package com.ItsTime.ItNovation.controller.mail;

import com.ItsTime.ItNovation.domain.mail.dto.CodeCheckRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.PasswordFindRequestDto;
import com.ItsTime.ItNovation.domain.mail.dto.RewritePasswordRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.mail.EmailService;
import com.ItsTime.ItNovation.service.user.UserService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private Map<String, String> authCodeMap = new ConcurrentHashMap<>(); // 동시성 문제가 있을거 같아 ConcurrentHashMap 사용해서 멀티 스레드에서 무사히 돌아갈 수 있도록 진행..

    @PostMapping("/passwordfind/emailSend")
    public ResponseEntity<String> mailConfirm(@RequestBody PasswordFindRequestDto emailDto){
        try {
            String authCode = emailService.sendMail(emailDto.getEmail());
            String email = emailDto.getEmail();
            authCodeMap.put(email, authCode);
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



//    @PostMapping("/passwordfind/rewritePw")
//    public ResponseEntity<String> rewritePassword(@RequestBody RewritePasswordRequestDto rewritePasswordRequestDto){
//        String email = rewritePasswordRequestDto.getEmail();
//        String password = rewritePasswordRequestDto.getPassword();
//        User findUser = userRepository.findByEmail(email).get(); // -> 이 과정까지 온 것 자체가 무조건 이메일이 있어서 올 수 있는 과정
//        // 이후 작업 -> User entity의 update를 활용하여 password를 업데이트 하는 과정이 필요함. 생각할 부분 암호화 하는 것이 필요함.
//        //findUser.
//    }

}