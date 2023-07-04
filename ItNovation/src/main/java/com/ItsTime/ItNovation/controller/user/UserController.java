package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.domain.user.dto.SignUpRequestDto;
import com.ItsTime.ItNovation.domain.user.dto.SignUpResponseDto;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDto;
import com.ItsTime.ItNovation.domain.user.dto.UserProfileDtoMe;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.user.UserProfileService;
import com.ItsTime.ItNovation.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("회원가입");
        return userService.join(signUpRequestDto);
    }


    @PostMapping("/userProfile/me")
    public ResponseEntity userProfileMe(@RequestBody UserProfileDtoMe userProfileDtoMe, Authentication authentication) {
        log.info("userProfileMe");
        String email = authentication.getName(); // 현재 사용자의 이메일 추출

        // 사용자 정보 업데이트 및 서비스 호출
        userProfileService.userProfileMe(userProfileDtoMe, email);

        // 성공적으로 처리되었음을 나타내는 200 상태 코드 반환
        return ResponseEntity.ok().build();
    }
    @PutMapping("/userProfile")
    public ResponseEntity userProfile(@RequestBody UserProfileDto userProfileDto) {
        log.info("userProfile");
        return userProfileService.userProfile(userProfileDto);
    }
}
