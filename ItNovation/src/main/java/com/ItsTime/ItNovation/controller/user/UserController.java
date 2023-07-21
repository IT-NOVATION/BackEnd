package com.ItsTime.ItNovation.controller.user;


import com.ItsTime.ItNovation.domain.user.dto.*;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.user.UserLoginStateService;
import com.ItsTime.ItNovation.service.user.UserProfileService;
import com.ItsTime.ItNovation.service.user.UserService;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final JwtService jwtService;
    private final UserLoginStateService userLoginStateService;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("회원가입");
        return userService.join(signUpRequestDto);
    }
    @GetMapping("/custom-logout")
    public ResponseEntity logout(HttpServletRequest request) {
        log.info("로그아웃");
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if(accessToken.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }else{
            log.info("엑세스 토큰: {}" ,accessToken.get());

            return userService.logout(accessToken.get());
        }

    }


    @PutMapping("/userProfile/me")
    public ResponseEntity userProfileMe(@RequestBody UserProfileDtoMe userProfileDtoMe, Authentication authentication) {
        log.info("userProfileMe");
        String email = authentication.getName(); // 현재 사용자의 이메일 추출


        // 사용자 정보 업데이트 및 서비스 호출
        return userProfileService.userProfileMe(userProfileDtoMe, email);


    }
    @PutMapping("/userProfile")
    public ResponseEntity userProfile(@RequestBody UserProfileDto userProfileDto) {
        log.info("userProfile");
        return userProfileService.userProfile(userProfileDto);
    }
    @GetMapping("/loginState")
    public ResponseEntity<LoginStateDto> userLoginState(HttpServletRequest request) {
        log.info("loginstate");
        try {
            Optional<String> accessToken = jwtService.extractAccessToken(request);
            if (accessToken.isEmpty()) {
                return userLoginStateService.loginState(null);
            } else {
                return userLoginStateService.loginState(accessToken.get());
            }
        }catch (JWTDecodeException e) {
            log.error("토큰 추출 오류: " + e.getMessage());
            return userLoginStateService.loginState(null);

        }


    }
}
