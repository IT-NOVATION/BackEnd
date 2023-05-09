package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.domain.user.dto.SignUpRequestDto;
import com.ItsTime.ItNovation.domain.user.dto.SignUpResponseDto;
import com.ItsTime.ItNovation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.join(signUpRequestDto);
    }

    //jwt는 JSON 데이터를 Base64 URL-safe Encode를 통해 인코딩하여 직렬화한 것
    //header: base64UrlEncode를 사용하여 암호화
    //payload: base64UrlEncode를 사용하여 암호화
    //signature: 서버가 가지고 있는 개인키를 통해 암호화되어있음, 외부에서 복호화 불가능
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwt 인증 성공";
    }
}
