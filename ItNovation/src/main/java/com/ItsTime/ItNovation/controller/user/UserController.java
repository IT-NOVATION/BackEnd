package com.ItsTime.ItNovation.controller.user;


import com.ItsTime.ItNovation.domain.user.dto.*;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.user.UserLoginStateService;
import com.ItsTime.ItNovation.service.user.UserProfileService;
import com.ItsTime.ItNovation.service.user.UserService;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name="사용자 API")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final JwtService jwtService;
    private final UserLoginStateService userLoginStateService;


    @PutMapping("/auth/profile")
    @Operation(summary = "무비로그에서 사용자 프로필 수정 하기")
    public ResponseEntity userProfileMe(@RequestBody UserProfileDtoMe userProfileDtoMe,
        Authentication authentication) {
        log.info("userProfileMe");
        String email = authentication.getName(); // 현재 사용자의 이메일 추출

        // 사용자 정보 업데이트 및 서비스 호출
        return userProfileService.userProfileMe(userProfileDtoMe, email);


    }

    @PutMapping("/profile")
    @Operation(summary = "사용자 프로필 수정 하기")

    public ResponseEntity userProfile(@RequestBody UserProfileDto userProfileDto) {
        log.info("userProfile");
        return userProfileService.userProfile(userProfileDto);
    }

    @GetMapping("/state")
    @Operation(summary = "사용자 로그인 상태")
    public ResponseEntity<LoginStateDto> userLoginState(HttpServletRequest request) {
        log.info("loginstate");
        try {
            Optional<String> accessToken = jwtService.extractAccessToken(request);
            if (accessToken.isEmpty()) {
                return userLoginStateService.loginState(null);
            } else {
                return userLoginStateService.loginState(accessToken.get());
            }
        } catch (JWTDecodeException e) {
            log.error("토큰 추출 오류: " + e.getMessage());
            return userLoginStateService.loginState(null);
        }

    }
}
