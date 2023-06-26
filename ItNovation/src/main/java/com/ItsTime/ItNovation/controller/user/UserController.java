package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.common.dto.ApiResult;
import com.ItsTime.ItNovation.domain.user.dto.SignUpRequestDto;
import com.ItsTime.ItNovation.domain.user.dto.SignUpResponseDto;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.user.UserProfileService;
import com.ItsTime.ItNovation.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("회원가입");
        return userService.join(signUpRequestDto);
    }

    @PostMapping("/userProfileInfo")
    public ResponseEntity userProfile(@RequestBody UserProfileDto userProfileDto, HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);

        if(accessToken.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }else{
            return userProfileService.userProfile(userProfileDto, accessToken.get());
        }
    }
    @GetMapping("/profile")
    public String secondSignup() {
        return "리다이렉트";
    }
}
